package com.example.hoonsletter_back_springboot.repository;

import com.example.hoonsletter_back_springboot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
