package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.LetterScene;
import java.util.List;

public record LetterSceneDto(
    Long id,
    int order,
    LetterDto letterDto,
    List<SceneMessageDto> messageDtos,
    List<ScenePictureDto> pictureDtos
) {
  public static LetterSceneDto of(Long id,
      int order,
      LetterDto letterDto,
      List<SceneMessageDto> messageDtos,
      List<ScenePictureDto> pictureDtos){
    return new LetterSceneDto(id,
        order,
        letterDto,
        messageDtos,
        pictureDtos);
  }


  public static LetterSceneDto from(LetterScene entity) {
    return new LetterSceneDto(
        entity.getId(),
        entity.getOrder(),
        LetterDto.from(entity.getLetter()),
        entity.getSceneMessages().stream()
            .map(SceneMessageDto::from)
            .toList(),
        entity.getScenePictures().stream()
            .map(ScenePictureDto::from)
            .toList()
    );
  }

  public LetterScene toEntity(Letter letter){
    return LetterScene.of(order, letter);
  }
}
