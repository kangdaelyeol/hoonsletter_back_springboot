package com.example.hoonsletter_back_springboot.dto.response;

import com.example.hoonsletter_back_springboot.domain.constant.LetterType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.LetterSceneDto;
import com.example.hoonsletter_back_springboot.dto.SceneMessageDto;
import com.example.hoonsletter_back_springboot.dto.ScenePictureDto;
import java.util.Comparator;
import java.util.List;

public record GetLetterResponse(
    LetterType type,
    List<Scene> sceneList
) {

  public record Scene (
      List<MessageContent> messageList,
      List<String> pictureList
  ){

    public record MessageContent(
        String content,
        MessageSizeType sizeType,
        MessageColorType colorType
    ) {
      public static MessageContent from(SceneMessageDto dto) {
        return new MessageContent(
            dto.content(), dto.sizeType(), dto.colorType()
        );
      }
    }

    public static Scene from(LetterSceneDto dto) {
      List<SceneMessageDto> sortedMessageDtoList = dto.messageDtos().stream().sorted(Comparator.comparingInt(SceneMessageDto::order)).toList(); // order 순서대로 정렬
      List<ScenePictureDto> sortedPictureDtoList = dto.pictureDtos().stream().sorted(Comparator.comparingInt(ScenePictureDto::order)).toList(); // order 순서대로 정렬

      List<MessageContent> messageList = sortedMessageDtoList.stream().map(MessageContent::from).toList();
      List<String> pictureList = sortedPictureDtoList.stream().map(ScenePictureDto::url).toList();

      return new Scene(messageList, pictureList);
    }
  }

  public static GetLetterResponse from(LetterDto dto) {
    return new GetLetterResponse(
        dto.letterType(),
        dto.letterSceneDtos().stream().map(Scene::from).toList()
    );
  }


}
