package com.example.hoonsletter_back_springboot.domain.constant;

import lombok.Getter;

@Getter
public enum SearchDirectionType {
  ASC("asc"),
  DESC("desc");

  private final String searchType;

  SearchDirectionType(String searchType) {
    this.searchType = searchType;
  }

}
