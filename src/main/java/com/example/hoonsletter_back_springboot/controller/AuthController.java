package com.example.hoonsletter_back_springboot.controller;


import com.example.hoonsletter_back_springboot.config.JwtTokenProvider;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.dto.JwtToken;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.dto.request.SignInRequest;
import com.example.hoonsletter_back_springboot.dto.response.CurrentUserResponse;
import com.example.hoonsletter_back_springboot.service.UserAccountService;
import com.example.hoonsletter_back_springboot.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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
    this.userAccountService = userAccountService;
  }

  @PostMapping("/sign-in")
  public ResponseEntity<JwtToken> signIn(@RequestBody SignInRequest signInRequest){
    JwtToken token = userAccountService.signInUser(signInRequest.username(), signInRequest.password());

    return ResponseEntity.ok(token);
  }

  @PostMapping("/current-user")
  public ResponseEntity<CurrentUserResponse> getAuth(){
    String username = SecurityUtil.getCurrentUsername();
    return ResponseEntity.ok(CurrentUserResponse.from(userAccountService.getUserAccount(username)));
  }

  @PostMapping("/success")
  public String test(){
    return "success";
  }
}
