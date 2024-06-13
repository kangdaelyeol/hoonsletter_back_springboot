package com.example.hoonsletter_back_springboot.repository;

import com.example.hoonsletter_back_springboot.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {

}
