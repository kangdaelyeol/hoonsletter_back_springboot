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
      List<SceneMessageDto> sortedMessageDtoList = dto.messageDtoList().stream().sorted(Comparator.comparingInt(SceneMessageDto::partOrder)).toList(); // partOrder 순서대로 정렬
      List<ScenePictureDto> sortedPictureDtoList = dto.pictureDtoList().stream().sorted(Comparator.comparingInt(ScenePictureDto::partOrder)).toList(); // partOrder 순서대로 정렬

      List<MessageContent> messageList = sortedMessageDtoList.stream().map(MessageContent::from).toList();
      List<String> pictureList = sortedPictureDtoList.stream().map(ScenePictureDto::url).toList();

      return new Scene(messageList, pictureList);
    }


  }

  public static GetLetterResponse from(LetterDto dto) {
    List<LetterSceneDto> sortedSceneList = dto.letterSceneDtoList().stream().sorted(Comparator.comparingInt(LetterSceneDto::partOrder)).toList();
    return new GetLetterResponse(
        dto.letterType(),
        sortedSceneList.stream().map(Scene::from).toList()
    );
  }


}
