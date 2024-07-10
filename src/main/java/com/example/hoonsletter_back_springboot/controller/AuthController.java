package com.example.hoonsletter_back_springboot.controller;


import com.example.hoonsletter_back_springboot.dto.JwtToken;
import com.example.hoonsletter_back_springboot.dto.request.SignInRequest;
import com.example.hoonsletter_back_springboot.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UserAccountService userAccountService;

  @Autowired
  public AuthController(UserAccountService userAccountService) {
    this. userAccountService = userAccountService;
  }

  @PostMapping("/sign-in")
  public ResponseEntity<JwtToken> signIn(@RequestBody SignInRequest signInRequest){
    JwtToken token = userAccountService.signInUser(signInRequest.username(), signInRequest.password());

    return ResponseEntity.ok(token);
  }

  @PostMapping("/success")
  public String test(){
    return "success";
  }
}
