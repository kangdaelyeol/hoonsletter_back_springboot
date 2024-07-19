package com.example.hoonsletter_back_springboot.dto.request;

import com.example.hoonsletter_back_springboot.dto.UserAccountDto;

public record UserUpdateRequest (
    String username,
    String nickname,
    String profileUrl
){
  public UserAccountDto toDto(){
    return UserAccountDto.of(username, null, nickname, profileUrl);
  }
}
