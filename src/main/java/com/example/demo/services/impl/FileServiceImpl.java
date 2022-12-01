package com.example.demo.services.impl;

import com.example.demo.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {

        // File name(abc.json)
        String filename = file.getOriginalFilename();

        // random name generate file(to eliminate different file with same name)
        String randomID = UUID.randomUUID().toString();
        String uniqueFileName = randomID.concat(filename.substring(filename.lastIndexOf(".")));

        // Full path
        String filePath = path + File.separator + uniqueFileName;

        // create folder if not created
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return uniqueFileName;
    }

    @Override
    public InputStream downloadFile(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        InputStream is = new FileInputStream(filePath);
        // db logic to return inpustream
        return is;
    }
}
