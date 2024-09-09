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
import com.example.hoonsletter_back_springboot.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class LetterService {

  private final LetterRepository letterRepository;

  private final UserAccountRepository userAccountRepository;

  private final FileStorageService fileStorageService;

  public LetterDto saveLetter(LetterDto dto){
    UserAccount userAccount = userAccountRepository.getReferenceById(SecurityUtil.getCurrentUsername());

    Letter letter = dto.toEntity(userAccount);
    List<LetterSceneDto> letterSceneDtos = dto.letterSceneDtos();
    List<LetterScene> letterScenes = new ArrayList<>();

    letterSceneDtos.forEach(sceneDto -> {
      LetterScene scene = createLetterScene(sceneDto, letter);
      letterScenes.add(scene);
    });

    letter.setLetterScenes(letterScenes);
    Letter savedLetter = letterRepository.save(letter);
    return LetterDto.from(savedLetter);
  }

  @Transactional(readOnly = true)
  public LetterDto getLetter(Long letterId) {
    return letterRepository.findById(letterId)
        .map(LetterDto::from)
        .orElseThrow(() -> new EntityNotFoundException("편지를 찾을 수 없습니다 - letterId: " + letterId));
  }

  public void deleteLetter(Long letterId) {
    String username = SecurityUtil.getCurrentUsername();
    Letter letter = letterRepository.findById(letterId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다!" + letterId));

    if(!letter.getUserAccount().getUsername().equals(username)) return;

    List<String> pictureList = getPictureList(letter);

    pictureList.forEach(fileName -> {
      try{
        fileStorageService.deleteFileByName(fileName);
      } catch (IOException e) {
        log.warn("파일 삭제 실패 - fileName: " + fileName);
      }
    });

    letterRepository.deleteByIdAndUserAccount_Username(letterId, username);
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

  public void updateLetter(Long letterId, LetterDto dto){
    try {
      Letter letter = letterRepository.getReferenceById(letterId);
      // check updatable
      if(!letter.isUpdatable()) return;

      UserAccount userAccount = userAccountRepository.getReferenceById(SecurityUtil.getCurrentUsername());
      // check equality
      if(!letter.getUserAccount().equals(userAccount)) return;

      if(dto.title() != null){
        letter.setTitle(dto.title());
      }

      if(dto.thumbnailUrl() != null){
        letter.setThumbnailUrl(dto.thumbnailUrl());
      }

      letter.getLetterScenes().clear();

      dto.letterSceneDtos().forEach(letterScene -> {
        LetterScene scene = createLetterScene(letterScene, letter);
        letter.addLetterScene(scene);
      });
      
    } catch (EntityNotFoundException e){
      log.warn("게시글 업데이트 실패. 업데이트에 필요한 정보를 찾을 수 없습니다 - ", e.getLocalizedMessage());
    }
  }

  private static LetterScene createLetterScene(LetterSceneDto sceneDto, Letter letter) {
    List<SceneMessage> sceneMessages = new ArrayList<>();
    List<ScenePicture> scenePictures = new ArrayList<>();
    LetterScene scene = sceneDto.toEntity(letter);

    sceneDto.messageDtos()
        .forEach(messageDto -> {
          SceneMessage message = messageDto.toEntity(scene);
          sceneMessages.add(message);
        });

    sceneDto.pictureDtos()
        .forEach(pictureDto -> {
          ScenePicture picture = pictureDto.toEntity(scene);
          String originalFileName = getOriginalFileNameWithExtension(pictureDto.url());
          picture.setUrl(originalFileName);
          scenePictures.add(picture);
        });

    scene.setSceneMessages(sceneMessages);
    scene.setScenePictures(scenePictures);

    return scene;
  }

  private static List<String> getPictureList(Letter letter) {
    List<String> pictureList = new ArrayList<>(letter.getLetterScenes()
        .stream()
        .flatMap(scene -> scene.getScenePictures().stream())
        .map(ScenePicture::getUrl)
        .toList());


    pictureList.add(letter.getThumbnailUrl());

    return pictureList;
  }

  private static String getOriginalFileNameWithExtension(String filePath) {
    Path path = Paths.get(filePath);
    return path.getFileName().toString();
  }
}
