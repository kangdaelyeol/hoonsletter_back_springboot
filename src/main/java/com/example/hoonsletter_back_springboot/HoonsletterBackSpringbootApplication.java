package com.example.hoonsletter_back_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HoonsletterBackSpringbootApplication {

  public static void main(String[] args) {
    SpringApplication.run(HoonsletterBackSpringbootApplication.class, args);
  }

}
