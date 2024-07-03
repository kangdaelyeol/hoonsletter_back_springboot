package com.example.hoonsletter_back_springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import net.bytebuddy.implementation.bytecode.Throw;
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
    String rawPassword = "testPw";
    String encodedPw = "abcd1234abcd1234asdf";
    UserAccountDto dto = UserAccountDto.of(
        "testUsername",
        rawPassword,
        "testNickname",
        "testThumbnail"
        );
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
    UserAccountDto dto = UserAccountDto.of(
        "short",
        "rawPassword",
        "testNickname",
        "testThumbnail"
    );

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
    UserAccountDto dto = UserAccountDto.of(
        "testUsername",
        "rawPassword",
        "testNickname",
        "testThumbnail"
    );
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
}