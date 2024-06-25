package com.example.hoonsletter_back_springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("JPA 연결 테스트")
@DataJpaTest
class JpaRepositoryTest {
  final UserAccountRepository userAccountRepository;
  final LetterRepository letterRepository;
  final LetterSceneRepository letterSceneRepository;
  final SceneMessageRepository sceneMessageRepository;
  final ScenePictureRepository scenePictureRepository;

  JpaRepositoryTest(
      @Autowired UserAccountRepository userAccountRepository,
      @Autowired LetterRepository letterRepository,
      @Autowired LetterSceneRepository letterSceneRepository,
      @Autowired SceneMessageRepository sceneMessageRepository,
      @Autowired ScenePictureRepository scenePictureRepository
  ) {
    this.userAccountRepository = userAccountRepository;
    this.letterRepository = letterRepository;
    this.letterSceneRepository = letterSceneRepository;
    this.sceneMessageRepository = sceneMessageRepository;
    this.scenePictureRepository = scenePictureRepository;
  }

  @DisplayName("select test")
  @Test
  void givenTestData_whenSelecting_thenWorksFine() {
    // Given

    // When
    List<UserAccount> userAccountList = userAccountRepository.findAll();
    List<LetterScene> letterSceneList = letterSceneRepository.findAll();
    List<Letter> letterList = letterRepository.findAll();
    // Then
    assertThat(userAccountList).hasSize(4);
    assertThat(letterSceneList).hasSize(11);
    assertThat(letterList).hasSize(5);
  }

  @DisplayName("delete test")
  @Test
  void givenNothing_whenDeleting_thenWorksFine() {
    // Given

    // When
    userAccountRepository.deleteAll();

    // Then
    assertThat(userAccountRepository.count()).isEqualTo(0);
    assertThat(letterRepository.count()).isEqualTo(0);
    assertThat(letterSceneRepository.count()).isEqualTo(0);
    assertThat(sceneMessageRepository.count()).isEqualTo(0);
    assertThat(scenePictureRepository.count()).isEqualTo(0);
  }

  @DisplayName("insert test")
  void givenNothing_whenInserting_thenWorksFine() {
    // Given
    long prevCount = userAccountRepository.count();
    UserAccount userAccount = UserAccount.of(
        "testuser",
        "testPassword",
        "testNickname",
        "testUrl"
    );

    // When
    userAccountRepository.save(userAccount);

    // Then
    assertThat(userAccountRepository.count()).isEqualTo(prevCount + 1);
  }

  @DisplayName("update test")
  @Test
  void givenTestData_whenUpdating_thenWorksFine() {
    // Given
    Letter letter = letterRepository.findById(1L).orElseThrow(
        () -> new EntityNotFoundException("Letter not Found")
    ); // ID가 존재하지 않을 경우 예외를 던져주어야 함.
    String newTitle = "newtitle";
    letter.setTitle(newTitle);

    // When
    Letter savedLetter = letterRepository.saveAndFlush(letter);

    // Then
    assertThat(savedLetter.getTitle()).isEqualTo(newTitle);
  }
}
