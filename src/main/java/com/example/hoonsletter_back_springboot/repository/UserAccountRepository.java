package com.example.hoonsletter_back_springboot.repository;

import com.example.hoonsletter_back_springboot.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
  boolean existsByNickname(String nickname);
}
