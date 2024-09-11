package com.example.hoonsletter_back_springboot.service;


import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {
  public static final int BAR_LENGTH = 5;

  public List<Integer> getPaginationBarNumberList(int currentPageNumber, int totalPages) {
    int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
    int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);
    return IntStream.range(startNumber + 1, endNumber + 1).boxed().toList();
  }

  public int currentBarLength() {
    return BAR_LENGTH;
  }
}
