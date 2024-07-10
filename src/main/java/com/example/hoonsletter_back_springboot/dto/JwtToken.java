package com.example.hoonsletter_back_springboot.dto;

import lombok.Builder;

@Builder
public record JwtToken(
    String grantType,
    String accessToken,
    String refreshToken
) {
}
