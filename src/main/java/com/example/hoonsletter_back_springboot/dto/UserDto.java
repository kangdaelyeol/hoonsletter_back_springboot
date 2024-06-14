package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.User;
import java.util.List;

public record UserDto(
    String username,
    String password,
    String nickname,
    String profileUrl,
    List<LetterDto> letterDtos
) {
  public static UserDto of(String username,
      String password,
      String nickname,
      String profileUrl,
      List<LetterDto> letterDtos){
    return new UserDto(username,
        password,
        nickname,
        profileUrl,
        letterDtos);
  }

  public static UserDto from(User entity){
    return new UserDto(
        entity.getUsername(),
        entity.getPassword(),
        entity.getNickname(),
        entity.getProfileUrl(),
        entity.getLetters().stream()
            .map(LetterDto::from)
            .toList()
    );
  }

  public User toEntity(){
    return User.of(username, password, nickname, profileUrl);
  }

}
