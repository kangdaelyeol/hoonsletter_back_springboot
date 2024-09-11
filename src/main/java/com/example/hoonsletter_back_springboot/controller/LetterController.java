package com.example.hoonsletter_back_springboot.controller;

import com.example.hoonsletter_back_springboot.domain.constant.SearchDirectionType;
import com.example.hoonsletter_back_springboot.domain.constant.SearchType;
import com.example.hoonsletter_back_springboot.domain.constant.SortType;
import com.example.hoonsletter_back_springboot.dto.LetterDto;
import com.example.hoonsletter_back_springboot.dto.request.LetterIdRequest;
import com.example.hoonsletter_back_springboot.dto.request.LetterUpdateRequest;
import com.example.hoonsletter_back_springboot.dto.request.LetterSaveRequest;
import com.example.hoonsletter_back_springboot.dto.response.GetLetterResponse;
import com.example.hoonsletter_back_springboot.dto.response.LetterInfoResponse;
import com.example.hoonsletter_back_springboot.dto.response.LetterSearchResponse;
import com.example.hoonsletter_back_springboot.service.LetterService;
import com.example.hoonsletter_back_springboot.service.PaginationService;
import java.util.List;
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
  private final PaginationService paginationService;

  @Autowired
  public LetterController(LetterService letterService,
      PaginationService paginationService){
    this.letterService = letterService;
    this.paginationService = paginationService;
  }

  @GetMapping("/get")
  public ResponseEntity<GetLetterResponse> getLetter(@RequestParam Long letterId){
    return ResponseEntity.ok(GetLetterResponse.from(letterService.getLetter(letterId)));
  }

  @GetMapping("/search")
  public ResponseEntity<LetterSearchResponse> searchLetter(
      @RequestParam(defaultValue = "") String keyword,
      @RequestParam(defaultValue = "CREATED_AT") SortType sortBy,
      @RequestParam(defaultValue = "DESC") SearchDirectionType direction,
      @RequestParam(defaultValue = "TITLE") SearchType searchType,
      @RequestParam(defaultValue = "0") int page)  {

    Sort sort = direction.getSearchType().equalsIgnoreCase(Direction.ASC.name())
        ? Sort.by(sortBy.getDbOption()).ascending()
        : Sort.by(sortBy.getDbOption()).descending();

    Pageable pageable = PageRequest.of(page, 10, sort);

    Page<LetterInfoResponse> letterInfoResponsePage = letterService.searchLetters(searchType, keyword, pageable).map(LetterInfoResponse::from);
    List<Integer> paginationBarNumberList = paginationService.getPaginationBarNumberList(pageable.getPageNumber(),
        letterInfoResponsePage.getTotalPages());

    return ResponseEntity.ok(LetterSearchResponse.of(letterInfoResponsePage, paginationBarNumberList));
  }

  @PostMapping("/delete")
  public ResponseEntity<Void> deleteLetter(@RequestBody LetterIdRequest request){
    letterService.deleteLetter(request.letterId());
    return ResponseEntity.status(200).build();
  }

  @PostMapping("/update")
  public ResponseEntity<Void> updateLetter(@RequestBody LetterUpdateRequest request) {
    letterService.updateLetter(request.letterId(), request.toDto());
    return ResponseEntity.status(200).build();
  }

  @PostMapping("/create")
  public ResponseEntity<LetterDto> createLetter(@RequestBody LetterSaveRequest request) {
    LetterDto savedLetterDto = letterService.saveLetter(request.toDto());
    return ResponseEntity.ok(savedLetterDto);
  }
}
