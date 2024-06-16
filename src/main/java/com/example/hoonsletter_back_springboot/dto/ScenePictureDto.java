package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.ScenePicture;

public record ScenePictureDto(
    Long id,
    int order,
    String url,
    LetterSceneDto sceneDto
) {
  public static ScenePictureDto of(Long id, int order, String url, LetterSceneDto sceneDto){
    return new ScenePictureDto(id, order, url, sceneDto);
  }

  public static ScenePictureDto from(ScenePicture entity){
    return ScenePictureDto.of(
        entity.getId(),
        entity.getPartOrder(),
        entity.getUrl(),
        LetterSceneDto.from(entity.getLetterScene())
    );
  }

  public ScenePicture toEntity(LetterScene letterScene){
    return ScenePicture.of(
        order,
        url,
        letterScene
    );
  }
}
