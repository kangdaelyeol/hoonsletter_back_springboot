package com.example.hoonsletter_back_springboot.controller;

import com.example.hoonsletter_back_springboot.dto.response.GetLetterResponse;
import com.example.hoonsletter_back_springboot.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/letter")
public class LetterController {
  private final LetterService letterService;

  @Autowired
  public LetterController(LetterService letterService){
    this.letterService = letterService;
  }

  @GetMapping("/get")
  public ResponseEntity<GetLetterResponse> getLetter(@RequestParam Long letterId){
    return ResponseEntity.ok(GetLetterResponse.from(letterService.getLetter(letterId)));
  }


}
