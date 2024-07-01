package com.example.hoonsletter_back_springboot.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

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
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
  LetterService sut;

  @Mock
  LetterRepository letterRepository;
  @Mock
  UserAccountRepository userAccountRepository;
  @Mock
  LetterSceneRepository letterSceneRepository;
  @Mock
  SceneMessageRepository sceneMessageRepository;
  @Mock
  ScenePictureRepository scenePictureRepository;

  @DisplayName("편지 정보를 입력하면 편지를 저장합니다.")
  @Test
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


  @DisplayName("편지 id정보로 조회하면, 편지 정보를 반환한다.")
  @Test
  void givenLetterId_whenSearching_thenReturnsLetterInfo(){
    // Given
    Long letterId = 10L;
    String username = "testuser";
    Letter letter = createLetter(username);
    given(letterRepository.findById(letterId)).willReturn(Optional.of(letter));

    // When
    LetterDto dto = sut.getLetter(letterId);

    // Then
    assertThat(dto)
        .hasFieldOrPropertyWithValue("title", letter.getTitle())
        .hasFieldOrPropertyWithValue("letterType", letter.getType())
        .hasFieldOrPropertyWithValue("thumbnailUrl", letter.getThumbnailUrl())
        .hasFieldOrPropertyWithValue("createdAt", letter.getCreatedAt())
        .hasFieldOrPropertyWithValue("updatable", letter.isUpdatable())
        .hasFieldOrPropertyWithValue("username", letter.getUserAccount().getUsername())
        .hasFieldOrPropertyWithValue("letterSceneDtos", letter.getLetterScenes().stream()
            .map(LetterSceneDto::from)
            .toList());
    then(letterRepository).should().findById(letterId);
  }

  @DisplayName("id에 대한 편지 정보가 없으면 예외를 던진다.")
  @Test
  void givenNonexistentLetterId_whenSearching_thenThrowsException() {
    // Given
    Long letterId = 0L;
    given(letterRepository.findById(letterId)).willReturn(Optional.empty());

    // When
    Throwable t = catchThrowable(() -> sut.getLetter(letterId));

    // Then
    assertThat(t)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("편지를 찾을 수 없습니다 - letterId: " + letterId);
    then(letterRepository).should().findById(letterId);
  }

  @DisplayName("편지의 id와 유저 아이디를 입력하면 편지를 삭제한다.")
  @Test
  void givenLetterIdAndUsername_whenDeleting_thenDeletesLetter(){
    // Given
    Long letterId = 1L;
    String username = "test1";
    willDoNothing().given(letterRepository).deleteByIdAndUserAccount_Username(letterId, username);
    willDoNothing().given(letterRepository).flush();

    // When
    sut.deleteLetter(letterId, username);

    // Then
    then(letterRepository).should().deleteByIdAndUserAccount_Username(letterId, username);
    then(letterRepository).should().flush();
  }

  Letter createLetter(String username) {
    UserAccount user = createUser(username);
    LetterDto letterDto = createLetterDto(user.getUsername());
    return Letter.of(
        letterDto.title(),
        letterDto.letterType(),
        letterDto.updatable(),
        letterDto.thumbnailUrl(),
        user
    );
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