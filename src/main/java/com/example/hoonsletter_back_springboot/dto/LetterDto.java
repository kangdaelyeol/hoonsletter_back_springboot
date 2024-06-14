package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.User;
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
    UserDto userDto,
    List<LetterSceneDto> letterSceneDtos
    ) {
  public static LetterDto of(
      Long id,
      String title,
      LetterType letterType,
      String thumbnailUrl,
      LocalDateTime createdAt,
      boolean updatable,
      UserDto user,
      List<LetterSceneDto> letterSceneDtos
  ){
    return new LetterDto(id,
        title,
        letterType,
        thumbnailUrl,
        createdAt,
        updatable,
        user,
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
        UserDto.from(entity.getUser()),
        entity.getLetterScenes().stream()
            .map(LetterSceneDto::from)
            .toList()
    );
  }
  public Letter toEntity(User user){
    return Letter.of(
        title,
        letterType,
        thumbnailUrl,
        user
    );
  }

}
