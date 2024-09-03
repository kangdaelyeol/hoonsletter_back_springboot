package com.example.hoonsletter_back_springboot.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  @Value(value = "${file-upload-dir}")
  private String uploadDirPath;

  public String UploadFile(MultipartFile file) throws IOException {
    String uniquePrefix = new Date().toString();
    String fileName = uniquePrefix + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    Path path = Paths.get(uploadDirPath + File.separator + fileName);

    Files.copy(file.getInputStream(), path);

    return fileName;
  }
}
