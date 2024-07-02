package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.domain.constant.LetterType;
import java.time.LocalDateTime;
import java.util.List;

public record LetterDto(
    Long id,
    String title,
    LetterType letterType,
    String thumbnailUrl,
    LocalDateTime createdAt,
    boolean updatable,
    UserAccountDto userAccountDto,
    List<LetterSceneDto> letterSceneDtos
    ) {
  public static LetterDto of(
      Long id,
      String title,
      LetterType letterType,
      String thumbnailUrl,
      LocalDateTime createdAt,
      boolean updatable,
      UserAccountDto userAccountDto,
      List<LetterSceneDto> letterSceneDtos
  ){
    return new LetterDto(id,
        title,
        letterType,
        thumbnailUrl,
        createdAt,
        updatable,
        userAccountDto,
        letterSceneDtos);
  }


  public static LetterDto from(Letter entity){
    return new LetterDto(
        entity.getId(),
        entity.getTitle(),
        entity.getType(),
        entity.getThumbnailUrl(),
        entity.getCreatedAt(),
        entity.isUpdatable(),
        UserAccountDto.from(entity.getUserAccount()),
        entity.getLetterScenes().stream()
            .map(LetterSceneDto::from)
            .toList()
    );
  }
  public Letter toEntity(UserAccount userAccount){
    return Letter.of(
        title,
        letterType,
        thumbnailUrl,
        userAccount
    );
  }

}
