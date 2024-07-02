package com.example.hoonsletter_back_springboot.service;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.LetterScene;
import com.example.hoonsletter_back_springboot.domain.SceneMessage;
import com.example.hoonsletter_back_springboot.domain.ScenePicture;
import com.example.hoonsletter_back_springboot.domain.UserAccount;
import com.example.hoonsletter_back_springboot.domain.constant.SearchType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.LetterSceneDto;
import com.example.hoonsletter_back_springboot.dto.SceneMessageDto;
import com.example.hoonsletter_back_springboot.dto.ScenePictureDto;
import com.example.hoonsletter_back_springboot.repository.LetterRepository;
import com.example.hoonsletter_back_springboot.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class LetterService {

  private final LetterRepository letterRepository;

  private final UserAccountRepository userAccountRepository;

  public void saveLetter(LetterDto dto){
    UserAccount userAccount = userAccountRepository.getReferenceById(dto.username());

    Letter letter = dto.toEntity(userAccount);
    List<LetterSceneDto> letterSceneDtos = dto.letterSceneDtos();
    List<LetterScene> letterScenes = new ArrayList<>();

    for(LetterSceneDto sceneDto : letterSceneDtos){

      LetterScene scene = createLetterScene(sceneDto, letter);
      letterScenes.add(scene);

    }
    letter.setLetterScenes(letterScenes);
    Letter savedLetter = letterRepository.save(letter);
  }

  @Transactional(readOnly = true)
  public LetterDto getLetter(Long letterId) {
    return letterRepository.findById(letterId)
        .map(LetterDto::from)
        .orElseThrow(() -> new EntityNotFoundException("편지를 찾을 수 없습니다 - letterId: " + letterId));
  }

  public void deleteLetter(Long letterId, String username) {
    letterRepository.deleteByIdAndUserAccount_Username(letterId, username);
    letterRepository.flush();
  }

  @Transactional(readOnly = true)
  public Page<LetterDto> searchLetters(SearchType searchType, String keyword, Pageable pageable){
    if(keyword == null || keyword.isBlank()){
      return letterRepository.findAll(pageable).map(LetterDto::from);
    }

    return switch (searchType){
      case TITLE ->
        letterRepository.findByTitleContaining(keyword, pageable).map(LetterDto::from);
      case NICKNAME ->
        letterRepository.findByUserAccount_NicknameContaining(keyword, pageable).map(LetterDto::from);
    };
  }



  private static LetterScene createLetterScene(LetterSceneDto sceneDto, Letter letter) {
    List<SceneMessage> sceneMessages = new ArrayList<>();
    List<ScenePicture> scenePictures = new ArrayList<>();
    LetterScene scene = sceneDto.toEntity(letter);

    for(SceneMessageDto messageDto : sceneDto.messageDtos()){
      SceneMessage message = messageDto.toEntity(scene);
      sceneMessages.add(message);
    }

    for(ScenePictureDto pictureDto : sceneDto.pictureDtos()){
      ScenePicture picture = pictureDto.toEntity(scene);
      scenePictures.add(picture);
    }

    scene.setSceneMessages(sceneMessages);
    scene.setScenePictures(scenePictures);
    return scene;
  }
}
