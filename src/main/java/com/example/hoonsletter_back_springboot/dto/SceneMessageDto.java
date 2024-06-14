package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.SceneMessage;
import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;

public record SceneMessageDto(
    Long id,
    int order,
    String content,
    MessageSizeType sizeType,
    MessageColorType colorType,
    LetterSceneDto sceneDto
    ) {
  public static SceneMessageDto of(Long id,
      int order,
      String content,
      MessageSizeType sizeType,
      MessageColorType colorType,
      LetterSceneDto sceneDto){
    return new SceneMessageDto(id, order, content, sizeType, colorType, sceneDto);
  }

  public static SceneMessageDto from(SceneMessage entity){
    return SceneMessageDto.of(
        entity.getId(),
        entity.getOrder(),
        entity.getContent(),
        entity.getSizeType(),
        entity.getColorType(),
        LetterSceneDto.from(entity.getLetterScene())
    );
  }
  public SceneMessage toEntity(LetterScene letterScene){
    return  SceneMessage.of(order, content, sizeType, colorType, letterScene);
  }
}
