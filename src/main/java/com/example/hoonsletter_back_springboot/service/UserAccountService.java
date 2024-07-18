package com.example.hoonsletter_back_springboot.service;


import com.example.hoonsletter_back_springboot.config.JwtTokenProvider;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.dto.JwtToken;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.dto.UserAccountWithLettersDto;
import com.example.hoonsletter_back_springboot.dto.request.ChangePasswordRequest;
import com.example.hoonsletter_back_springboot.dto.request.SignUpRequest;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import com.example.hoonsletter_back_springboot.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserAccountService {
  private final UserAccountRepository userAccountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Autowired
  UserAccountService(UserAccountRepository userAccountRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider,
      AuthenticationManagerBuilder authenticationManagerBuilder
      ){
    this.userAccountRepository = userAccountRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
  }

  @Transactional(readOnly = true)
  public UserAccountDto getUserAccount(String username) {
    UserAccount user = userAccountRepository.findById(username)
        .orElseThrow(() -> new EntityNotFoundException("유저 정보를 찾을 수 없습니다 - " + username));

    return UserAccountDto.from(user);
  }

  @Transactional(readOnly = true)
  public UserAccountWithLettersDto getUserAccountWithLetter(String username) {
    UserAccount user = userAccountRepository.findById(username)
        .orElseThrow(() -> new EntityNotFoundException("유저 정보를 찾을 수 없습니다 - " + username));
    return UserAccountWithLettersDto.from(user);
  }

  public UserAccountDto saveUser(SignUpRequest dto){

    if(!dto.password().equals(dto.confirmPassword())){
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다!");
    }

    if(userAccountRepository.existsById(dto.username().trim())){
      throw new IllegalArgumentException("이미 존재하는 사용자 Id 입니다 - " + dto.username());
    }

    if(
        userAccountRepository.existsByNickname(dto.nickname().trim())
        || userAccountRepository.existsById(dto.nickname().trim())
    )
    {
      throw new IllegalArgumentException("이미 존재하는 닉네임 입니다 - " + dto.nickname());
    }

    if(dto.username().trim().length() < 8){
      throw new IllegalArgumentException("아이디는 최소 8자 이상입니다.");
    }

    if(dto.username().trim().contains(" ")){
      throw new IllegalArgumentException("아이디에 공백이 포함되면 안됩니다.");
    }

    if(dto.password().trim().length() < 8){
      throw new IllegalArgumentException("비밀번호는 최소 8자 이상입니다.");
    }

    if(dto.password().trim().contains(" ")){
      throw new IllegalArgumentException("비밀번호에 공백이 포함되면 안됩니다.");
    }

    if(!dto.nickname().trim().isEmpty() && dto.nickname().trim().contains(" ")){
      throw new IllegalArgumentException("닉네임에 공백이 포함되면 안됩니다.");
    }

    String newPassword = passwordEncoder.encode(dto.password().trim());
    String nickname;
    String profileUrl;
    if(dto.nickname().trim().isBlank() || dto.nickname().trim().isEmpty())
      nickname = dto.username();
    else nickname = dto.nickname();

    if(dto.profileUrl().trim().isEmpty() || dto.profileUrl().trim().isBlank())
      profileUrl = "defaultProfileUrl";
    else
      profileUrl = dto.profileUrl();


    UserAccount userAccount = UserAccount.of(
        dto.username(),
        newPassword,
        nickname,
        profileUrl
    );

    UserAccount savedUser = userAccountRepository.save(userAccount);

    return UserAccountDto.from(savedUser);
  }

  public void deleteUser(){
    String username = SecurityUtil.getCurrentUsername();
    try{
      userAccountRepository.deleteById(username);
    } catch (IllegalArgumentException e){
      log.warn("유저 정보가 존재하지 않습니다");
    }
  }

  public void updateUser(UserAccountDto dto){
    String username = SecurityUtil.getCurrentUsername();
    if(!username.equals(dto.username())) return;

    if(dto.nickname().trim().contains(" ")){
      throw new IllegalArgumentException("닉네임에 공백이 포함되면 안됩니다.");
    }

    if(userAccountRepository.existsByNickname(dto.nickname().trim())
        || (userAccountRepository.existsById(dto.nickname().trim()) && !dto.nickname().trim()
        .equals(username))){
      throw new IllegalArgumentException("중복된 닉네임 입니다 - " + dto.nickname().trim());
    }

    String nickname = dto.nickname().trim().isBlank()
        ? username
        : dto.nickname().trim();

    String profileUrl = dto.profileUrl().trim().isBlank()
        ? "defaultProfileUrl"
        : dto.profileUrl().trim();

    UserAccount userAccount = userAccountRepository.getReferenceById(username);
    userAccount.setNickname(nickname);
    userAccount.setProfileUrl(profileUrl);
  }

  public void changePassword(ChangePasswordRequest dto){
    String username = SecurityUtil.getCurrentUsername();
    UserAccount userAccount = userAccountRepository.getReferenceById(username);

    if(dto.currentPassword().contains(" ") || dto.newPassword().contains(" ") || dto.confirmPassword().contains(" ")){
      throw new IllegalArgumentException("패스워드에 공백이 포함되면 안됩니다!");
    }

    if(dto.newPassword().length() < 8){
      throw new IllegalArgumentException("패스워드는 최소 8자 이상입니다!");
    }

    if(!dto.newPassword().equals(dto.confirmPassword())){
      throw new IllegalArgumentException("패스워드 확인이 일치하지 않습니다!");
    }

    if(!passwordEncoder.matches(dto.currentPassword(), userAccount.getPassword())){
      throw new IllegalArgumentException("기존 패스워드가 일치하지 않습니다!");
    }

    if(dto.currentPassword().equals(dto.newPassword())){
      throw new IllegalArgumentException("기존의 비밀번호와 같은 비밀번호로 변경할 수 없습니다!");
    }

    userAccount.setPassword(passwordEncoder.encode(dto.newPassword()));
  }

  public JwtToken signInUser(String username, String password) {
    // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
    // 이 때 authentication은 인증 여부를 확인하는 authenticated 값이 false
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

    // 2. 실제 검증(사용자 비밀번호 체크)이 이루어지는 부분
    // authenticate 메서드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 메서드가 실행
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 3. 인증 정보를 기반으로 JWT 토큰 생성
    return jwtTokenProvider.generateToken(authentication);
  }
}
