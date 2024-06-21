package com.example.hoonsletter_back_springboot.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.domain.constant.LetterType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.LetterSceneDto;
import com.example.hoonsletter_back_springboot.dto.SceneMessageDto;
import com.example.hoonsletter_back_springboot.dto.ScenePictureDto;
import com.example.hoonsletter_back_springboot.dto.UserDto;
import com.example.hoonsletter_back_springboot.repository.LetterRepository;
import com.example.hoonsletter_back_springboot.repository.LetterSceneRepository;
import com.example.hoonsletter_back_springboot.repository.SceneMessageRepository;
import com.example.hoonsletter_back_springboot.repository.ScenePictureRepository;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Business Logic - Letter")
@ExtendWith(MockitoExtension.class) // Unit Test for service layer
class LetterServiceTest {

  @InjectMocks
  private LetterService sut;

  @Mock
  private LetterRepository letterRepository;
  @Mock
  private UserAccountRepository userAccountRepository;
  @Mock
  private LetterSceneRepository letterSceneRepository;
  @Mock
  private SceneMessageRepository sceneMessageRepository;
  @Mock
  private ScenePictureRepository scenePictureRepository;

  @Test
  @DisplayName("편지 정보를 입력하면 편지를 저장합니다.")
  void givenLetterInfo_whenSavingLetter_thenSavesLetter(){
    // Given
    String username = "testuser";
    LetterDto letterDto = createLetterDto(username);
    given(userAccountRepository.getReferenceById(letterDto.username())).willReturn(createUser(username));


    // When
    sut.saveLetter(letterDto);

    // Then
    then(userAccountRepository).should().getReferenceById(letterDto.username());
    then(letterRepository).should().save(any(Letter.class));

  }

  LetterDto createLetterDto(String username) {
    Long letterId = 10L;
    List<LetterSceneDto> letterSceneDtos = createLetterSceneDtoList(letterId);

    return LetterDto.of(
      letterId,
        "testTitle",
        LetterType.TYPE1,
        "testThumbnail",
        LocalDateTime.now(),
        true,
        username,
        letterSceneDtos
    );
  }

  List<LetterSceneDto> createLetterSceneDtoList(Long letterId) {
    int size = 3;
    List<LetterSceneDto> letterSceneDtoList = new ArrayList<>();
    for(int i = 100 ; i < 100 + size; i++){
      Long sceneId = (long)i; // implicit auto boxing "Long.valueOf(i)" instead
      List<SceneMessageDto> messageDtoList = createMessageDtoList(sceneId);
      List<ScenePictureDto> pictureDtoList = createPictureDtoList(sceneId);

      LetterSceneDto letterSceneDto = LetterSceneDto.of(
          sceneId,
          i,
          letterId,
          messageDtoList,
          pictureDtoList
      );
      letterSceneDtoList.add(letterSceneDto);
    }
    return letterSceneDtoList;
  }

  List<SceneMessageDto> createMessageDtoList(Long sceneId) {
    List<SceneMessageDto> messageDtoList = new ArrayList<>();
    int size = 3;
    for(int i = 100; i < 100 + size; i++) {
      Long messageId = (long) i;
      SceneMessageDto messageDto = SceneMessageDto.of(
          messageId,
          i,
          "testContent",
          MessageSizeType.MEDIUM,
          MessageColorType.BLACK,
          sceneId
      );
      messageDtoList.add(messageDto);
    }
    return messageDtoList;
  }

  List<ScenePictureDto> createPictureDtoList(Long sceneId) {
    List<ScenePictureDto> pictureDtoList = new ArrayList<>();
    int size = 3;
    for(int i = 100; i < 100 + size; i++){
      Long pictureId = (long) i;
      ScenePictureDto pictureDto = ScenePictureDto.of(
          pictureId,
          i,
          "testPictureUrl",
          sceneId
      );
      pictureDtoList.add(pictureDto);
    }
    return pictureDtoList;
  }

  UserDto createUserDto() {
    String username = "testuser";
    return UserDto.of(
        username,
        "testpw",
        "testnickname",
        "testprofile",
        List.of(createLetterDto(username))
    );
  }

  UserAccount createUser(String username) {
    return UserAccount.of(username,
        "password",
        "testnickname",
        "testprofile"
        );
  }
}