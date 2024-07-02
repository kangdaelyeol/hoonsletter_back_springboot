package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import java.util.List;

public record UserDto(
    String username,
    String password,
    String nickname,
    String profileUrl
) {
  public static UserDto of(String username,
      String password,
      String nickname,
      String profileUrl
      ){
    return new UserDto(username,
        password,
        nickname,
        profileUrl
        );
  }

  public static UserDto from(UserAccount entity){
    return new UserDto(
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
