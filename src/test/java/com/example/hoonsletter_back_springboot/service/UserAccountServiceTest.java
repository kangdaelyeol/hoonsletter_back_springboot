package com.example.hoonsletter_back_springboot.service;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.dto.UserAccountWithLettersDto;
import com.example.hoonsletter_back_springboot.dto.request.ChangePasswordRequest;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    UserAccountDto dto = createUserAccountDto("short", "password");

    given(userAccountRepository.existsById(dto.username().trim())).willReturn(false);
    given(userAccountRepository.existsById(dto.nickname().trim())).willReturn(false);
    given(userAccountRepository.existsByNickname(dto.nickname().trim())).willReturn(false);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("아이디는 최소 8자 이상입니다.");
    then(passwordEncoder).shouldHaveNoInteractions();
    then(userAccountRepository).should().existsById(dto.username().trim());
    then(userAccountRepository).should().existsByNickname(dto.nickname().trim());
    then(userAccountRepository).should().existsById(dto.nickname().trim());
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
    given(userAccountRepository.existsById(dto.username().trim())).willReturn(false);
    given(userAccountRepository.existsById(dto.nickname().trim())).willReturn(false);
    given(userAccountRepository.existsByNickname(dto.nickname().trim())).willReturn(false);
    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("아이디에 공백이 포함되면 안됩니다.");
    then(userAccountRepository).should().existsById(dto.username().trim());
    then(userAccountRepository).should().existsByNickname(dto.nickname().trim());
    then(userAccountRepository).should().existsById(dto.nickname().trim());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
  }

  @DisplayName("유저 비밀번호 길이가 작으면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithShortPassword_whenSavingUserAccount_thenThrowsException() {
    // Given
    UserAccountDto dto = createUserAccountDto("testUsername", "short");
    given(userAccountRepository.existsById(dto.username().trim())).willReturn(false);
    given(userAccountRepository.existsById(dto.nickname().trim())).willReturn(false);
    given(userAccountRepository.existsByNickname(dto.nickname().trim())).willReturn(false);

    // When
    Throwable t = catchThrowable(() -> sut.saveUser(dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("비밀번호는 최소 8자 이상입니다.");
    then(userAccountRepository).should().existsById(dto.username().trim());
    then(passwordEncoder).shouldHaveNoInteractions();
    then(userAccountRepository).should().existsByNickname(dto.nickname().trim());
    then(userAccountRepository).should().existsById(dto.nickname().trim());
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

  @DisplayName("유저의 아이디를 입력하면 유저를 삭제한다")
  @Test
  void givenUsername_whenDeletingUserAccount_thenDeletesUserAccount(){
    // Given
    String username = "testUsername";
    willDoNothing().given(userAccountRepository).deleteById(username);

    // When
    sut.deleteUser(username);

    // Then
    then(userAccountRepository).should().deleteById(username);
  }

  @DisplayName("유저 정보를 입력하면 유저 정보를 수정한다")
  @Test
  void givenUserAccountInfo_whenUpdatingUserAccount_thenUpdatesUserAccount(){
    // Given
    String username = "testUser";
    UserAccount userAccount = createUserAccount(username);
    UserAccountDto dto = createUserAccountDto(username,
        "password",
        "newNickname",
        "newProfileUrl");
    given(userAccountRepository.getReferenceById(username)).willReturn(userAccount);
    given(userAccountRepository.existsByNickname(dto.nickname())).willReturn(false);

    // When
    sut.updateUser(username, dto);

    // Then
    then(userAccountRepository).should().flush();
    then(userAccountRepository).should().getReferenceById(username);
    then(userAccountRepository).should().existsByNickname(dto.nickname());
    assertThat(userAccount)
        .hasFieldOrPropertyWithValue("nickname", "newNickname")
        .hasFieldOrPropertyWithValue("profileUrl", "newProfileUrl");
  }

  @DisplayName("중복된 닉네임으로 유저를 수정하면 예외를 던진다")
  @Test
  void givenUserAccountInfoWithExistsNickname_whenUpdatingUserAccount_thenThrowsException() {
    // Given
    String username = "testUser";
    UserAccountDto dto = createUserAccountDto(username, "testPassword");

    given(userAccountRepository.existsByNickname(dto.nickname().trim())).willReturn(true);

    // When
    Throwable t = catchThrowable(() -> sut.updateUser(username, dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("중복된 닉네임 입니다 - " + dto.nickname().trim());
    then(userAccountRepository).should().existsByNickname(dto.nickname().trim());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
  }

  @DisplayName("공백이 포함된 닉네임으로 수정시 예외를 던진다")
  @Test
  void givenUserAccountInfoWithNicknameContainsBlank_whenSavingUserAccount_thenThrowsException() {
    // Given
    String username = "testUser";
    String nickname = "b lank nickname";
    UserAccountDto dto = createUserAccountDto(username, "password", nickname, "profile");

    // When
    Throwable t = catchThrowable(() -> sut.updateUser(username, dto));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("닉네임에 공백이 포함되면 안됩니다.");

    then(userAccountRepository).shouldHaveNoInteractions();
  }

  @DisplayName("비밀번호 변경 정보를 입력하면 비밀번호를 수정한다.")
  @Test
  void givenChangePasswordInfo_whenChangingPassword_thenChangesPassword() {
    // Given
    UserAccount userAccount = createUserAccount("testUser");
    String currentPassword = "testPassword";
    String currentEncodedPassword = userAccount.getPassword();
    String newPassword = "newPassword";
    String newEncodedPassword = "newEncodedPassword";
    ChangePasswordRequest request = ChangePasswordRequest.of(
        currentPassword,
        newPassword,
        newPassword
    );
    given(passwordEncoder.matches(request.currentPassword(), userAccount.getPassword())).willReturn(true);
    given(passwordEncoder.encode(request.newPassword())).willReturn(newEncodedPassword);
    given(userAccountRepository.getReferenceById(userAccount.getUsername())).willReturn(userAccount);

    // When
    sut.changePassword(userAccount.getUsername(), request);

    // Then
    assertThat(userAccount)
        .hasFieldOrPropertyWithValue("password", newEncodedPassword);
    then(passwordEncoder).should().matches(request.currentPassword(), currentEncodedPassword);
    then(passwordEncoder).should().encode(request.newPassword());
    then(userAccountRepository).should().flush();
  }

  static Stream<Arguments> givenChangePasswordInfoContainingSpace_whenChangingPassword_thenThrowsException() {
    return Stream.of(
        Arguments.arguments("c urrentpassword", "newpassword", "newpassword"),
        Arguments.arguments("currentpassword", "n ewpassword", "newpassword"),
        Arguments.arguments("currentpassword", "newpassword", "n ewpassword"),
        Arguments.arguments("currentpassword", "newpassword", "newpassword "),
        Arguments.arguments("currentpassword", " newpassword", "newpassword"),
        Arguments.arguments(" currentpassword", "newpassword", "newpassword"),
        Arguments.arguments("curr entpas sword", "newpa ssword", "ne wpasswor d")
    );
  }

  @DisplayName("비밀번호 입력 정보에 공백이 포함되면 예외를 던진다")
  @MethodSource
  @ParameterizedTest(name = "[test - {index}] currentPassword = {0}, newPassword = {1}, confirmPassword = {2}")
  void givenChangePasswordInfoContainingSpace_whenChangingPassword_thenThrowsException(
      String currentPassword,
      String newPassword,
      String confirmPassword
  ) {
    // Given
    UserAccount userAccount = createUserAccount();
    ChangePasswordRequest request = ChangePasswordRequest.of(
        currentPassword,
        newPassword,
        confirmPassword
    );
    given(userAccountRepository.getReferenceById(userAccount.getUsername())).willReturn(userAccount);

    // When
    Throwable t = catchThrowable(() -> sut.changePassword(userAccount.getUsername(), request));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("패스워드에 공백이 포함되면 안됩니다!");
    then(userAccountRepository).should().getReferenceById(userAccount.getUsername());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
    then(passwordEncoder).shouldHaveNoInteractions();
  }

  @DisplayName("새로운 패스워드와 패스워드확인이 다르면 예외를 던진다")
  @Test
  void givenDifferentChangePasswordInfo_whenChangingPassword_thenThrowsException() {
    // Given
    UserAccount userAccount = createUserAccount();
    ChangePasswordRequest request = ChangePasswordRequest.of(
        "password",
        "newPassword",
        "diffPassword"
    );
    given(userAccountRepository.getReferenceById(userAccount.getUsername())).willReturn(userAccount);

    // When
    Throwable t = catchThrowable(() -> sut.changePassword(userAccount.getUsername(), request));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("패스워드 확인이 일치하지 않습니다!");
    then(userAccountRepository).should().getReferenceById(userAccount.getUsername());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
  }

  @DisplayName("현재 패스워드의 입력이 일치하지 않으면 예외를 던진다")
  @Test
  void givenDifferentCurrentPasswordInfo_whenChangingPassword_thenThrowsException() {
    // Given
    UserAccount userAccount = createUserAccount();
    String diffPassword = "diffPassword";
    ChangePasswordRequest request = ChangePasswordRequest.of(
        diffPassword,
        "newPassword",
        "newPassword"
    );

    given(passwordEncoder.matches(diffPassword, userAccount.getPassword())).willReturn(false);
    given(userAccountRepository.getReferenceById(userAccount.getUsername())).willReturn(userAccount);

    // When
    Throwable t = catchThrowable(() -> sut.changePassword(userAccount.getUsername(), request));

    // Then
    assertThat(t)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("기존 패스워드가 일치하지 않습니다!");

    then(userAccountRepository).should().getReferenceById(userAccount.getUsername());
    then(passwordEncoder).should().matches(diffPassword, userAccount.getPassword());
    then(userAccountRepository).shouldHaveNoMoreInteractions();
    then(passwordEncoder).shouldHaveNoMoreInteractions();
  }

  UserAccount createUserAccount() {
    return UserAccount.of(
        "testUser",
        "encodedPassword",
        "testNickname",
        "testProfileUrl"
    );
  }

  UserAccountDto createUserAccountDto(String username, String password, String nickname, String profileUrl){
    return UserAccountDto.of(
        username,
        password,
        nickname,
        profileUrl
    );
  }
  UserAccountDto createUserAccountDto(String username, String password){
    return createUserAccountDto(
        username,
        password,
        "testNickname",
        "testProfileUrl"
    );
  }

  UserAccount createUserAccount(String username){
    return UserAccount.of(
        username,
        "password",
        "nickname",
        "profile"
    );
  }

  UserAccountDto createUserAccountDto(){
    return createUserAccountDto("testUsername", "testPassword");
  }
}