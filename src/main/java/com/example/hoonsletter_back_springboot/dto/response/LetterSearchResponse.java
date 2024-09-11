package com.example.hoonsletter_back_springboot.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record LetterSearchResponse(
    Page<LetterInfoResponse> letterInfoPage,
    List<Integer> paginationBarList
) {
  public static LetterSearchResponse of(Page<LetterInfoResponse> letterInfoPage,
      List<Integer> paginationBarList) {
    return new LetterSearchResponse(letterInfoPage,
        paginationBarList);
  }

}
