package com.example.hoonsletter_back_springboot.service;


import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.dto.UserAccountDto;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService {
  private final UserAccountRepository userAccountRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  UserAccountService(UserAccountRepository userAccountRepository,
      PasswordEncoder passwordEncoder
      ){
    this.userAccountRepository = userAccountRepository;
    this.passwordEncoder = passwordEncoder;
  }


  public void saveUser(UserAccountDto dto){
    if(userAccountRepository.existsById(dto.username())){
      throw new IllegalArgumentException("이미 존재하는 사용자 Id 입니다 - " + dto.username());
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
  }
}
