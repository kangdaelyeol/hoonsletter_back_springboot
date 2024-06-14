package com.example.hoonsletter_back_springboot.repository;

import com.example.hoonsletter_back_springboot.domain.SceneMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterMessageRepository extends JpaRepository<SceneMessage, Long> {

}
