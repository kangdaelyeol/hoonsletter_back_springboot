package com.example.hoonsletter_back_springboot.dto.request;

public record SignUpRequest(
    String username,
    String password,
    String confirmPassword,
    String nickname,
    String profileUrl
) {
  public static SignUpRequest of(String username,
      String password,
      String confirmPassword,
      String nickname,
      String profileUrl) {
    return new SignUpRequest(username,
        password,
        confirmPassword,
        nickname,
        profileUrl);
  }
}
