package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.ScenePicture;

public record ScenePictureDto(
    Long id,
    int order,
    String url,
    Long sceneId
) {
  public static ScenePictureDto of(Long id, int order, String url, Long sceneId){
    return new ScenePictureDto(id, order, url, sceneId);
  }

  public static ScenePictureDto from(ScenePicture entity){
    return ScenePictureDto.of(
        entity.getId(),
        entity.getPartOrder(),
        entity.getUrl(),
        entity.getLetterScene().getId()
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
