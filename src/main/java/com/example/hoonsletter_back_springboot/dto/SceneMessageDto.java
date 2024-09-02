package com.example.hoonsletter_back_springboot.dto;

import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.SceneMessage;
import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;

public record SceneMessageDto(
    Long id,
    int partOrder,
    String content,
    MessageSizeType sizeType,
    MessageColorType colorType,
    Long sceneId
    ) {
  public static SceneMessageDto of(Long id,
      int partOrder,
      String content,
      MessageSizeType sizeType,
      MessageColorType colorType,
      Long sceneId){
    return new SceneMessageDto(id, partOrder, content, sizeType, colorType, sceneId);
  }

  public static SceneMessageDto from(SceneMessage entity){
    return SceneMessageDto.of(
        entity.getId(),
        entity.getPartOrder(),
        entity.getContent(),
        entity.getSizeType(),
        entity.getColorType(),
        entity.getLetterScene().getId()
    );
  }
  public SceneMessage toEntity(LetterScene letterScene){
    return  SceneMessage.of(partOrder(),
        content(),
        sizeType(),
        colorType(),
        letterScene);
  }
}
