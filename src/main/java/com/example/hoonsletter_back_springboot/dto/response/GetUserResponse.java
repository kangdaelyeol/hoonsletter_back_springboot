package com.example.hoonsletter_back_springboot.dto.response;

import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.UserAccountWithLettersDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record GetUserResponse(
    String username,
    String nickname,
    String profileUrl,
    List<LetterInfo> letters
) {
  public static GetUserResponse of(String username,
      String nickname,
      String profileUrl,
      List<LetterInfo> letters){
    return new GetUserResponse(username,
        nickname,
        profileUrl,
        letters);
  }
  public static GetUserResponse from (UserAccountWithLettersDto dto){
    List<LetterInfo> letters = dto.letterDtoList().stream().map(LetterInfo::from).collect(Collectors.toList());
    return GetUserResponse.of(dto.username(),
        dto.nickname(),
        dto.profileUrl(),
        letters);
  }

  public record LetterInfo(
      Long id,
      String title,
      String thumbnailUrl,
      LocalDateTime createdAt,
      boolean updatable
  ){
    public static LetterInfo of(Long id,
        String title,
        String thumbnailUrl,
        LocalDateTime createdAt,
        boolean updatable) {
      return new LetterInfo(id,
          title,
          thumbnailUrl,
          createdAt,
          updatable);
    }

    public static LetterInfo from(LetterDto dto) {
      return LetterInfo.of(
          dto.id(),
          dto.title(),
          dto.thumbnailUrl(),
          dto.createdAt(),
          dto.updatable()
      );
    }
  }
}
