package com.example.hoonsletter_back_springboot.dto.response;

import com.example.hoonsletter_back_springboot.dto.LetterDto;
import java.time.LocalDateTime;

public record LetterInfoResponse(
    Long id,
    String title,
    String thumbnailUrl,
    LocalDateTime createdAt,
    String createdBy,
    boolean updatable
) {
  public static LetterInfoResponse of(Long id,
      String title,
      String thumbnailUrl,
      LocalDateTime createdAt,
      String createdBy,
      boolean updatable) {
    return new LetterInfoResponse(id,
        title,
        thumbnailUrl,
        createdAt,
        createdBy,
        updatable);
  }

  public static LetterInfoResponse from(LetterDto dto) {
    return LetterInfoResponse.of(dto.id(),
        dto.title(),
        dto.thumbnailUrl(),
        dto.createdAt(),
        dto.userAccountDto().nickname(),
        dto.updatable());
  }
}
