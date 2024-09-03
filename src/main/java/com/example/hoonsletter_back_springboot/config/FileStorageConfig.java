package com.example.hoonsletter_back_springboot.config;

import jakarta.annotation.PostConstruct;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

  @Value("${file-upload-dir}")
  private String uploadDir;

  @PostConstruct
  public void init() {
    File dir = new File(uploadDir);
    if(!dir.exists()) {
      dir.mkdirs();
      System.out.println("Directory created at: " + dir.getAbsolutePath());
    }
  }
}
