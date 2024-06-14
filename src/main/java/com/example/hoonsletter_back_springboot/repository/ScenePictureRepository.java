package com.example.hoonsletter_back_springboot.repository;

import com.example.hoonsletter_back_springboot.domain.ScenePicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterPictureRepository extends JpaRepository<ScenePicture, Long> {

}
