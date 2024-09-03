package com.example.hoonsletter_back_springboot.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileStorageController {

  @Value(value = "${file-upload-dir}")
  private String uploadDirPath;


  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(
      @RequestParam(name = "file")MultipartFile file
  ) {
    if(file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 없습니다!");
    }

    try {
      String uniquePrefix = new Date().toString();
      String fileName = uniquePrefix + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
      Path path = Paths.get(uploadDirPath + File.separator + fileName);

      Files.copy(file.getInputStream(), path);

      return ResponseEntity.ok(fileName);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패 - " + e.getLocalizedMessage());
    }
  }
}
