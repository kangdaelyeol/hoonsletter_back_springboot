package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import java.util.List;

public record UserAccountWithLettersDto(
    String username,
    String password,
    String nickname,
    String profileUrl,
    List<LetterDto> letterDtoList
) {
  public static UserAccountWithLettersDto of(
      String username,
      String password,
      String nickname,
      String profileUrl,
      List<LetterDto> letterDtoList
  ) {
    return new UserAccountWithLettersDto(
       username,
       password,
       nickname,
       profileUrl,
       letterDtoList
    );
  }

  public static UserAccountWithLettersDto from(UserAccount entity){
    List<LetterDto> letterDtoList = entity.getLetterList() != null
        ? entity.getLetterList().stream().map(LetterDto::from).toList()
        : List.of();

    return new UserAccountWithLettersDto(
        entity.getUsername(),
        entity.getPassword(),
        entity.getNickname(),
        entity.getProfileUrl(),
        letterDtoList
    );
  }
}
