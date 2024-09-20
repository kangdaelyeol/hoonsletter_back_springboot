package com.example.hoonsletter_back_springboot.domain.constant;

import lombok.Getter;

@Getter
public enum SortType {
  CREATED_AT("createdAt"),
  NICKNAME("userAccount_nickname"),
  TITLE("title");

  private final String dbOption;

  SortType(String dbOption) {
    this.dbOption = dbOption;
  }

}
