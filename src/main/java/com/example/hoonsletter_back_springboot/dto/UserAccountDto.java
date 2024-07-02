package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.UserAccount;

public record UserAccountDto(
    String username,
    String password,
    String nickname,
    String profileUrl
) {
  public static UserAccountDto of(String username,
      String password,
      String nickname,
      String profileUrl
      ){
    return new UserAccountDto(username,
        password,
        nickname,
        profileUrl
        );
  }

  public static UserAccountDto from(UserAccount entity){
    return new UserAccountDto(
        entity.getUsername(),
        entity.getPassword(),
        entity.getNickname(),
        entity.getProfileUrl()
    );
  }

  public UserAccount toEntity(){
    return UserAccount.of(username, password, nickname, profileUrl);
  }

}
