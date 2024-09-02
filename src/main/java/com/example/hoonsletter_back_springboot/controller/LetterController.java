package com.example.hoonsletter_back_springboot.controller;

import com.example.hoonsletter_back_springboot.domain.constant.SearchType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.request.LetterIdRequest;
import com.example.hoonsletter_back_springboot.dto.request.LetterSaveRequest;
import com.example.hoonsletter_back_springboot.dto.response.GetLetterResponse;
import com.example.hoonsletter_back_springboot.dto.response.LetterInfoResponse;
import com.example.hoonsletter_back_springboot.service.LetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/letter")
public class LetterController {
  private final LetterService letterService;

  @Autowired
  public LetterController(LetterService letterService){
    this.letterService = letterService;
  }

  @GetMapping("/get")
  public ResponseEntity<GetLetterResponse> getLetter(@RequestParam Long letterId){
    return ResponseEntity.ok(GetLetterResponse.from(letterService.getLetter(letterId)));
  }

  @GetMapping("/search")
  public ResponseEntity<Page<LetterInfoResponse>> searchLetter(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "username") String sortBy,
      @RequestParam(defaultValue = "desc") String direction,
      @RequestParam(defaultValue = "") SearchType searchType,
      @RequestParam(defaultValue = "0") int page)  {
    Sort sort = direction.equalsIgnoreCase(Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, 10, sort);

    return ResponseEntity.ok(letterService.searchLetters(searchType, keyword, pageable).map(LetterInfoResponse::from));
  }

  @PostMapping("/delete")
  public ResponseEntity<Void> deleteLetter(@RequestBody LetterIdRequest request){
    letterService.deleteLetter(request.letterId());
    return ResponseEntity.status(200).build();
  }


  @PostMapping("/create")
  public ResponseEntity<LetterDto> createLetter(@RequestBody SaveLetterRequest request) {
    LetterDto savedLetterDto = letterService.saveLetter(request.toDto());
    return ResponseEntity.ok(savedLetterDto);
  }
}
