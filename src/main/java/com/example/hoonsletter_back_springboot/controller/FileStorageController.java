package com.example.hoonsletter_back_springboot.controller;


import com.example.hoonsletter_back_springboot.service.FileStorageService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
public class FileStorageController {

  @Value(value = "${file-upload-dir}")
  private String uploadDirPath;

  private FileStorageService fileStorageService;

  @Autowired
  public FileStorageController(FileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }



  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(
      @RequestParam(name = "file")MultipartFile file
  ) {
    if(file.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 없습니다!");
    }

    try {
      String fileName = fileStorageService.uploadFile(file);

      return ResponseEntity.ok(fileName);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패 - " + e.getLocalizedMessage());
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<Void> deleteFile(@RequestBody String fileName) {
    try {
      fileStorageService.deleteFile(fileName);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    return ResponseEntity.ok().build();
  }
}
