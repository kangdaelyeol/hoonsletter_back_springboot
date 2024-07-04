package com.example.hoonsletter_back_springboot.service;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.dto.UserAccountWithLettersDto;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


@DisplayName("Business Logic - UserAccount")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

  @InjectMocks
  UserAccountService sut;

  @Mock
  PasswordEncoder passwordEncoder;
  @Mock
  UserAccountRepository userAccountRepository;


  @DisplayName("유저 정보를 입력하면 유저 정보를 저장한다.")
  @Test
  void givenUserAccountInfo_whenSavingUserAccount_thenSavesUserAccount() {
    // Given
    String encodedPw = "abcd1234abcd1234asdf";
    UserAccountDto dto = createUserAccountDto();
    UserAccount userAccount = UserAccount.of(
        "testUsername",
        encodedPw,
        "testNickname",
        "testThumbnail"
    );
    given(passwordEncoder.encode(dto.password())).willReturn(encodedPw);
    given(userAccountRepository.save(any(UserAccount.class))).willReturn(userAccount);
    // When
    sut.saveUser(dto);

    // Then
    then(passwordEncoder).should().encode(dto.password());
    then(userAccountRepository).should().save(any(UserAccount.class));
  }

  @DisplayName("유저 아이디 길이가 작으면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithShortUsername_whenSavingUserAccount_thenThrowsException() {
    // Given
    UserAccountDto dto = createUserAccountDto();

    given(userAccountRepository.existsById(dto.username())).willReturn(false);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("아이디는 최소 8자 이상입니다.");
    then(passwordEncoder).shouldHaveNoInteractions();
    then(userAccountRepository).should().existsById(dto.username());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
  }

  @DisplayName("유저 아이디가 중복되면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithExistsUsername_whenSavingUserAccount_thenThrowsException() {
    // Given
    UserAccountDto dto = createUserAccountDto();
    given(userAccountRepository.existsById(dto.username())).willReturn(true);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("이미 존재하는 사용자 Id 입니다 - " + dto.username());
    then(userAccountRepository).should().existsById(dto.username());
    then(passwordEncoder).shouldHaveNoInteractions();
  }

  @DisplayName("유저 아이디 사이에 공백이 포함되면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithUsernameContainingSpace_whenSavingUserAccount_thenThrowsException() {
    // Given
    UserAccountDto dto = createUserAccountDto(" user name ", "password");
    given(userAccountRepository.existsById(dto.username())).willReturn(false);
    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("아이디에 공백이 포함되면 안됩니다.");
    then(userAccountRepository).should().existsById(dto.username());
  }

  @DisplayName("유저 비밀번호 길이가 작으면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithShortPassword_whenSavingUserAccount_thenThrowsException() {
    // Given
    UserAccountDto dto = createUserAccountDto("testUsername", "short");
    given(userAccountRepository.existsById(dto.username())).willReturn(false);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("비밀번호는 최소 8자 이상입니다.");
    then(userAccountRepository).should().existsById(dto.username());
    then(passwordEncoder).shouldHaveNoInteractions();
    then(userAccountRepository).shouldHaveNoMoreInteractions();
  }

  @DisplayName("유저 비밀번호 사이에 공백이 포함되면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithPasswordContainingSpace_whenSavingUserAccount_thenThrowsException(){
    // Given
    UserAccountDto dto = createUserAccountDto("username", "pass word");
    given(userAccountRepository.existsById(dto.username())).willReturn(false);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("비밀번호에 공백이 포함되면 안됩니다.");
    then(userAccountRepository).should().existsById(dto.username());
  }

  @DisplayName("유저 닉네임 사이에 공백이 포함되면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithNicknameContainingSpace_whenSavingUserAccount_thenThrowsException(){
    // Given
    UserAccountDto dto = createUserAccountDto("username", "password", "nick   name", "thumbnail");
    given(userAccountRepository.existsById(dto.username())).willReturn(false);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("닉네임에 공백이 포함되면 안됩니다.");
    then(userAccountRepository).should().existsById(dto.username());
  }

  @DisplayName("유저 닉네임이 중복되면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithExistsNickname_whenSavingUserAccount_thenThrowsException(){
    // Given
    UserAccountDto dto = createUserAccountDto();
    given(userAccountRepository.existsById(dto.username())).willReturn(false);
    given(userAccountRepository.existsByNickname(dto.nickname())).willReturn(true);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("이미 존재하는 닉네임 입니다 - " + dto.nickname());
    then(userAccountRepository).should().existsById(dto.username());
    then(userAccountRepository).should().existsByNickname(dto.nickname());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
  }

  @DisplayName("유저 정보를 조회하면 편지 정보를 함께 조회한다.")
  @Test
  void givenUsername_whenSearchingUserAccount_thenReturnsUserAccountWithLetters() {
    // Given
    UserAccount userAccount = createUserAccount();
    String username = userAccount.getUsername();
    given(userAccountRepository.findById(username)).willReturn(Optional.of(userAccount));

    // When
    UserAccountWithLettersDto dto = sut.getUserAccount(username);

    // Then
    then(userAccountRepository).should().findById(username);
    assertThat(dto)
        .hasFieldOrPropertyWithValue("username", userAccount.getUsername())
        .hasFieldOrPropertyWithValue("password", userAccount.getPassword())
        .hasFieldOrPropertyWithValue("profileUrl", userAccount.getProfileUrl())
        .extracting("letterDtos", as(InstanceOfAssertFactories.COLLECTION))
        .hasSize(0);
  }

  @DisplayName("존재하지 않는 유저 정보를 조회하면 예외를 던진다")
  @Test
  void givenNonExistUsername_whenSearchingUserAccount_thenThrowsException(){
    // Given
    String username = "nonExistUsername";
   given(userAccountRepository.findById(username)).willReturn(Optional.empty());

    // When
    Throwable t = catchThrowable(() -> sut.getUserAccount(username));

    // Then
    assertThat(t)
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("유저 정보를 찾을 수 없습니다 - " + username);
    then(userAccountRepository).should().findById(username);
  }

  UserAccount createUserAccount() {
    return UserAccount.of(
        "testUser",
        "testPassword",
        "testNickname",
        "testProfileUrl"
    );
  }

  UserAccountDto createUserAccountDto(String username, String password, String nickname, String thumbnail){
    return UserAccountDto.of(
        username,
        password,
        nickname,
        thumbnail
    );
  }
  UserAccountDto createUserAccountDto(String username, String password){
    return createUserAccountDto(
        username,
        password,
        "testNickname",
        "testThumbnail"
    );
  }
  UserAccountDto createUserAccountDto(){
    return createUserAccountDto("testUsername", "testpassword");
  }

}