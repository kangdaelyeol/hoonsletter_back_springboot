package com.example.hoonsletter_back_springboot.dto.response;

import com.example.hoonsletter_back_springboot.dto.UserAccountDto;

public record SignUpResponse(
    String username,
    String nickname,
    String profileUrl
) {
  public static SignUpResponse of(String username, String nickname, String profileUrl){
    return new SignUpResponse(username, nickname, profileUrl);
  }
  public static SignUpResponse from(UserAccountDto dto) {
    return SignUpResponse.of(
        dto.username(),
        dto.nickname(),
        dto.profileUrl()
    );
  }
}
