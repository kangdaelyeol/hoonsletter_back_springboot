package com.example.hoonsletter_back_springboot.service;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.domain.constant.LetterType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;
import com.example.hoonsletter_back_springboot.domain.constant.SearchType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.LetterSceneDto;
import com.example.hoonsletter_back_springboot.dto.SceneMessageDto;
import com.example.hoonsletter_back_springboot.dto.ScenePictureDto;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.repository.LetterRepository;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("Business Logic - Letter")
@ExtendWith(MockitoExtension.class) // Unit Test for service layer
class LetterServiceTest {

  @InjectMocks
  LetterService sut;

  @Mock
  LetterRepository letterRepository;
  @Mock
  UserAccountRepository userAccountRepository;

  @DisplayName("편지 정보를 입력하면 편지를 저장합니다.")
  @Test
  void givenLetterInfo_whenSavingLetter_thenSavesLetter(){
    // Given
    String username = "testuser";
    LetterDto letterDto = createLetterDto(username);
    UserAccount user = createUser(username);

    given(userAccountRepository.getReferenceById(letterDto.userAccountDto().username())).willReturn(user);

    // When
    sut.saveLetter(letterDto);

    // Then
    then(userAccountRepository).should().getReferenceById(letterDto.userAccountDto().username());
    then(letterRepository).should().save(any(Letter.class));
  }

  @DisplayName("검색어 없이 편지를 검색하면, 편지 페이지를 반환한다")
  @Test
  void givenNoSearchParameters_whenSearching_thenReturnsLetterPage(){
    // Given
    Pageable pageable = Pageable.ofSize(10);
    given(letterRepository.findAll(pageable)).willReturn(Page.empty());

    // When
    Page<LetterDto> letters = sut.searchLetters(null, null, pageable);

    // Then
    assertThat(letters).isEmpty();
    then(letterRepository).should().findAll(pageable);
  }

  @DisplayName("검색어와 함께 편지를 검색하면, 편지 페이지를 반환한다.")
  @Test
  void givenSearchParameters_whenSearching_thenReturnsLetterPage(){
    // Given
    Pageable pageable = Pageable.ofSize(10);
    SearchType searchType = SearchType.TITLE;
    String keyword = "testTitle";
    given(letterRepository.findByTitleContaining(keyword, pageable)).willReturn(Page.empty());

    // When
    Page<LetterDto> letters = sut.searchLetters(searchType, keyword, pageable);

    // Then
    assertThat(letters).isEmpty();
    then(letterRepository).should().findByTitleContaining(keyword, pageable);
  }

  @DisplayName("편지 id정보로 조회하면, 편지 정보를 반환한다.")
  @Test
  void givenLetterId_whenSearching_thenReturnsLetterInfo(){
    // Given
    Long letterId = 10L;
    String username = "testuser";
    Letter letter = createLetter(username, letterId);
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
        .hasFieldOrPropertyWithValue("userAccountDto", UserAccountDto.from(letter.getUserAccount()))
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

    // When
    sut.deleteLetter(letterId, username);

    // Then
    then(letterRepository).should().deleteByIdAndUserAccount_Username(letterId, username);
  }

  @DisplayName("편지의 수정 정보를 입력하면, 편지를 수정한다")
  @Test
  void givenLetterIdAndModifiedInfo_whenUpdatingLetter_thenUpdatesLetter() {
    // Given
    String username = "test";
    Long letterId = 10L;
    Letter letter = createLetter(username, letterId);
    LetterDto dto = createLetterDto(username, "newTitle", "newThumbnail", "newMessage");
    given(userAccountRepository.getReferenceById(dto.userAccountDto().username())).willReturn(createUser(username));
    given(letterRepository.getReferenceById(dto.id())).willReturn(letter);

    // When
    sut.updateLetter(letter.getId(), dto);

    // Then
    then(letterRepository).should().getReferenceById(dto.id());
    then(userAccountRepository).should().getReferenceById(username);
    assertThat(letter)
        .hasFieldOrPropertyWithValue("title", dto.title())
        .hasFieldOrPropertyWithValue("thumbnailUrl", dto.thumbnailUrl())
        .extracting("letterScenes", as(InstanceOfAssertFactories.COLLECTION))
        .hasSize(5);

    letter.getLetterScenes().forEach(letterScene -> {
      assertThat(letterScene).extracting("sceneMessages", as(InstanceOfAssertFactories.COLLECTION))
              .hasSize(3)
                  .extracting("content")
                      .containsExactly("newMessage", "newMessage", "newMessage");

      letterScene.getSceneMessages().forEach(message -> {
        assertThat(message)
            .hasFieldOrPropertyWithValue("content", "newMessage");
      });
    });
  }

  @DisplayName("편지가 수정 불가능한 상태이면, 편지를 수정하지 않는다.")
  @Test
  void givenNonUpdatableLetterIdAndModifiedInfo_whenUpdatingLetter_thenWillDoNothing(){
    // Given
    String username = "testUser";
    Long letterId = 10L;
    Letter letter = createLetter(username, letterId);
    LetterDto dto = createLetterDto(username, "newTitle", "newThumbnail", "newMessage");

    // set updatable
    ReflectionTestUtils.setField(letter, "updatable", false);

    given(letterRepository.getReferenceById(letterId)).willReturn(letter);


    // When
    sut.updateLetter(letter.getId(), dto);

    // Then
    then(letterRepository).should().getReferenceById(letter.getId());
    then(letterRepository).shouldHaveNoMoreInteractions();
    then(userAccountRepository).shouldHaveNoInteractions();
  }

  @DisplayName("사용자와 편지 소유자가 다르면, 편지를 수정하지 않는다")
  @Test
  void givenLetterInfoAndDiffUserAccountInfo_whenUpdatingLetter_thenWillDoNothing() {
    // Given
    String username = "testUser1";
    String diffUsername = "diffUser";
    Long letterId = 10L;
    Letter letter = createLetter(username, letterId);
    LetterDto dto = createLetterDto(diffUsername, "newTitle", "newThumb", "newMessage");

    given(letterRepository.getReferenceById(letterId)).willReturn(letter);
    given(userAccountRepository.getReferenceById(dto.userAccountDto().username())).willReturn(createUser(diffUsername));

    // When
    sut.updateLetter(letterId, dto);

    // Then
    then(letterRepository).should().getReferenceById(letterId);
    then(userAccountRepository).should().getReferenceById(dto.userAccountDto().username());
    then(letterRepository).shouldHaveNoMoreInteractions();
    assertThat(letter.getTitle()).isEqualTo("testTitle");
    assertThat(letter.getThumbnailUrl()).isEqualTo("testThumbnail");
  }

  @DisplayName("없는 편지정보를 입력하면, 경고 로그를 찍고 아무것도 하지 않는다")
  @Test
  void givenNonexistLetterId_whenUpdatingLetter_thenLogsWarningAndDoesNothing(){
    // Given
    LetterDto dto = createLetterDto("testUser");

    given(letterRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

    // When
    sut.updateLetter(dto.id(), dto);

    // Then
    then(letterRepository).should().getReferenceById(dto.id());
    then(letterRepository).shouldHaveNoMoreInteractions();
    then(userAccountRepository).shouldHaveNoInteractions();
  }


  Letter createLetter(String username, String title, String thumbnail, Long letterId){
    UserAccount user = createUser(username);
    LetterDto letterDto = createLetterDto(user.getUsername(), title, thumbnail);
    Letter letter = Letter.of(
        letterDto.title(),
        letterDto.letterType(),
        letterDto.updatable(),
        letterDto.thumbnailUrl(),
        user
    );
    ReflectionTestUtils.setField(letter, "id", letterId);
    return letter;
  }
  Letter createLetter(String username, Long letterId) {
    String title = "testTitle";
    String thumbnail = "testThumbnail";

    return createLetter(username, title, thumbnail, letterId);
  }

  LetterDto createLetterDto(String username, String title, String thumbnail, String message) {
    Long letterId = 10L;
    List<LetterSceneDto> letterSceneDtos = createLetterSceneDtoList(letterId, message);

    return LetterDto.of(
      letterId,
        title,
        LetterType.TYPE1,
        thumbnail,
        LocalDateTime.now(),
        true,
        createUserDto(username),
        letterSceneDtos
    );
  }
  LetterDto createLetterDto(String username, String title, String thumbnail) {
    return createLetterDto(username, title, thumbnail, "testContent");
  }
  LetterDto createLetterDto(String username, String message){
    String title = "testTitle";
    String thumbnail = "testThumbnail";
    return createLetterDto(username, title, thumbnail, message);
  }
  LetterDto createLetterDto(String username){
    return createLetterDto(username, "testContent");
  }


  List<LetterSceneDto> createLetterSceneDtoList(Long letterId, String message) {
    int size = 5;
    List<LetterSceneDto> letterSceneDtoList = new ArrayList<>();
    for(int i = 100 ; i < 100 + size; i++){
      Long sceneId = (long)i; // implicit auto boxing "Long.valueOf(i)" instead
      List<SceneMessageDto> messageDtoList = createMessageDtoList(sceneId, message);
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
  List<LetterSceneDto> createLetterSceneDtoList(Long letterId){
    return createLetterSceneDtoList(letterId, "testContent");
  }

  List<SceneMessageDto> createMessageDtoList(Long sceneId, String message) {
    List<SceneMessageDto> messageDtoList = new ArrayList<>();
    int size = 3;
    for(int i = 100; i < 100 + size; i++) {
      Long messageId = (long) i;
      SceneMessageDto messageDto = SceneMessageDto.of(
          messageId,
          i,
          message,
          MessageSizeType.MEDIUM,
          MessageColorType.BLACK,
          sceneId
      );
      messageDtoList.add(messageDto);
    }
    return messageDtoList;
  }
  List<SceneMessageDto> createMessageDtoList(Long sceneId) {
    return createMessageDtoList(sceneId, "testContent");
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

  UserAccountDto createUserDto(String username) {
    String title = "testTitlte";
    String thumbnail = "testThumbnail";
    return UserAccountDto.of(
        username,
        "testpw",
        "testnickname",
        "testprofile"
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