package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.LetterScene;
import java.util.List;

public record LetterSceneDto(
    Long id,
    int partOrder,
    Long letterId,
    List<SceneMessageDto> messageDtoList,
    List<ScenePictureDto> pictureDtoList
) {
  public static LetterSceneDto of(Long id,
      int partOrder,
      Long letterId,
      List<SceneMessageDto> messageDtoList,
      List<ScenePictureDto> pictureDtoList){
    return new LetterSceneDto(id,
        partOrder,
        letterId,
        messageDtoList,
        pictureDtoList);
  }


  public static LetterSceneDto from(LetterScene entity) {
    return new LetterSceneDto(
        entity.getId(),
        entity.getPartOrder(),
        entity.getLetter().getId(),
        entity.getSceneMessages().stream()
            .map(SceneMessageDto::from)
            .toList(),
        entity.getScenePictures().stream()
            .map(ScenePictureDto::from)
            .toList()
    );
  }

  public LetterScene toEntity(Letter letter){
    return LetterScene.of(partOrder(), letter);
  }
}
