package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import java.util.List;

public record UserAccountWithLettersDto(
    String username,
    String password,
    String nickname,
    String profileUrl,
    List<LetterDto> letterDtos
) {
  public static UserAccountWithLettersDto of(
      String username,
      String password,
      String nickname,
      String profileUrl,
      List<LetterDto> letterDtos
  ) {
    return new UserAccountWithLettersDto(
       username,
       password,
       nickname,
       profileUrl,
       letterDtos
    );
  }

  public static UserAccountWithLettersDto from(UserAccount entity){
    List<LetterDto> letterDtos = entity.getLetters() != null
        ? entity.getLetters().stream().map(LetterDto::from).toList()
        : List.of();

    return new UserAccountWithLettersDto(
        entity.getUsername(),
        entity.getPassword(),
        entity.getNickname(),
        entity.getProfileUrl(),
        letterDtos
    );
  }
}
