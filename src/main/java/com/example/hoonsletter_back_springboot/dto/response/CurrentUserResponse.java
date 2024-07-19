package com.example.hoonsletter_back_springboot.dto.response;

import com.example.hoonsletter_back_springboot.dto.UserAccountDto;

public record CurrentUserResponse(
    String username,
    String nickname,
    String profileUrl
) {
  public static CurrentUserResponse of(String username, String nickname, String profileUrl) {
    return new CurrentUserResponse(username, nickname, profileUrl);
  }

  public static CurrentUserResponse from(UserAccountDto dto) {
    return CurrentUserResponse.of(dto.username(),
        dto.nickname(),
        dto.profileUrl());
  }
}
