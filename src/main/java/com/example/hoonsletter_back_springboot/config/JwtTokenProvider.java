package com.example.hoonsletter_back_springboot.config;

import com.example.hoonsletter_back_springboot.dto.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
  private final Key key;

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  // Member 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
  public JwtToken generateToken(Authentication authentication) {
    // 권한 가져오기
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    long now = System.currentTimeMillis();
    long dayToMillis = 86400000;

    // AccessToken 생성
    Date accessTokenExpiresIn = new Date(now + dayToMillis * 7); // 7 day = 86400000 milliseconds
    String accessToken = Jwts.builder()
        .setSubject(authentication.getName())
        .claim("auth", authorities)
        .setExpiration(accessTokenExpiresIn)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    // RefreshToken 생성
    String refreshToken = Jwts.builder()
        .setExpiration(accessTokenExpiresIn)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    return JwtToken.builder()
        .grantType("Bearer")
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  // JwtToken을 복호화하여 토큰에 들어있는 정보(Claim / payload)를 꺼내는 메서드
  public Authentication getAuthentication(String accessToken) {
    // Jwt Token decryption
    Claims claims = parseClaims(accessToken);

    if(claims.get("auth") == null){
      throw new RuntimeException("권한 정보가 없는 토큰 입니다.");
    }

    // claims에서 권한 정보 가져오기 -> ROLE_USER
    Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .toList();

    // UserDetails 객체를 만들어서 Authentication return
    // UserDetails: interface, User: UserDetails를 구현한 class
    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  // 토큰 정보를 검증하는 메서드
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e){
      log.info("Invalid JWT Token, ", e);
    } catch (ExpiredJwtException e){
      log.info("Expired JWT Token, ", e);
    } catch(UnsupportedJwtException e){
      log.info("UnsupportedJwtException", e);
    } catch(IllegalArgumentException e) {
      log.info("JWT claims string is empty", e);
    };
    return false;
  }

  // access Token
  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(accessToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
