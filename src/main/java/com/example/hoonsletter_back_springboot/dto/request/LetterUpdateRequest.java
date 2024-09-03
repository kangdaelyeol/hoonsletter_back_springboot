package com.example.hoonsletter_back_springboot.dto.request;

import com.example.hoonsletter_back_springboot.domain.constant.LetterType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.LetterSceneDto;
import com.example.hoonsletter_back_springboot.dto.SceneMessageDto;
import com.example.hoonsletter_back_springboot.dto.ScenePictureDto;
import java.util.List;

public record LetterUpdateRequest(
    Long letterId,
    String title,
    LetterType type,
    String thumbnail,
    List<LetterScene> sceneList
) {
  public record LetterScene (
      int partOrder,
      List<SceneMessage> messageList,
      List<ScenePicture> pictureList
  ) {

    public record SceneMessage (
        int partOrder,
        String content,
        MessageSizeType sizeType,
        MessageColorType colorType
    ) {

      public SceneMessageDto toDto() {
        return SceneMessageDto.of(
            null,
            partOrder(),
            content(),
            sizeType(),
            colorType(),
            null
        );
      }

    }

    public record ScenePicture (
        int partOrder,
        String url
    ) {

      public ScenePictureDto toDto () {
        return ScenePictureDto.of(null,
            partOrder(),
            url(),
            null);
      }

    }

    public LetterSceneDto toDto() {
      return LetterSceneDto.of(null,
          partOrder(),
          null,
          messageList().stream().map(SceneMessage::toDto).toList(),
          pictureList().stream().map(ScenePicture::toDto).toList());
    }
  }

  public LetterDto toDto() {
    return LetterDto.of(null,
        title(),
        type(),
        thumbnail(),
        null,
        true,
        null,
        sceneList().stream().map(LetterScene::toDto).toList());
  }
}
