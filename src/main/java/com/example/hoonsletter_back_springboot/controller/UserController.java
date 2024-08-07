package com.example.hoonsletter_back_springboot.controller;


import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.dto.UserAccountWithLettersDto;
import com.example.hoonsletter_back_springboot.dto.request.SignUpRequest;
import com.example.hoonsletter_back_springboot.dto.request.UserUpdateRequest;
import com.example.hoonsletter_back_springboot.dto.request.UsernameRequest;
import com.example.hoonsletter_back_springboot.dto.response.GetUserResponse;
import com.example.hoonsletter_back_springboot.dto.response.SignUpResponse;
import com.example.hoonsletter_back_springboot.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

  private final UserAccountService userAccountService;

  @Autowired
  public UserController(UserAccountService userAccountService){
    this.userAccountService = userAccountService;
  }

  @PostMapping("/sign-up")
  public ResponseEntity<SignUpResponse> signUpUser(@RequestBody SignUpRequest request){
    UserAccountDto dto = userAccountService.saveUser(request);

    return ResponseEntity.ok(SignUpResponse.from(dto));
  }

  @PostMapping("/get")
  public ResponseEntity<GetUserResponse> getUser(@RequestBody UsernameRequest request) {
    UserAccountWithLettersDto dto = userAccountService.getUserAccountWithLetter(request.username());
    return ResponseEntity.ok(GetUserResponse.from(dto));
  }

  @PostMapping("/delete")
  public ResponseEntity<Void> deleteUser(@RequestBody UsernameRequest request) {
    userAccountService.deleteUser();
    return ResponseEntity.status(200).build();
  }

  @PostMapping("/update")
  public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRequest request){
    userAccountService.updateUser(request.toDto());
    return ResponseEntity.status(200).build();
  }
}
