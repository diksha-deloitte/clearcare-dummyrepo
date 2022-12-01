package com.example.demo.controllers;

import com.example.demo.payload.FileResponse;
import com.example.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/file")
public class FileController {


    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;

    //upload file

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("file") MultipartFile file){

        //validation
        if(file.isEmpty()){
            return new ResponseEntity<FileResponse>(new FileResponse(null,"Request must contain file"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //validation of file type
        if(!file.getContentType().equals("application/json")){
            return new ResponseEntity<FileResponse>(new FileResponse(null,"Only Json files can be uploaded"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String fileName = null;
        try {
            fileName = this.fileService.uploadFile(path, file);
            //db operations can be done here
            //call repository and save filename in db

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<FileResponse>(new FileResponse(null,"File is not uploaded due to error on server!"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<FileResponse>(new FileResponse(fileName,"File uploaded successfully"),HttpStatus.OK);
    }


    //download file

    @GetMapping(value = "/download/{fileName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void downloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException
    {
        InputStream resource = this.fileService.downloadFile(path, fileName);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

}
