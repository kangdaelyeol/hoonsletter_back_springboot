package com.example.hoonsletter_back_springboot.repository;

import com.example.hoonsletter_back_springboot.domain.Letter;
import com.example.hoonsletter_back_springboot.domain.QLetter;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface LetterRepository extends JpaRepository<Letter, Long>,
    QuerydslBinderCustomizer<QLetter>,
    QuerydslPredicateExecutor<Letter> {

  void deleteByIdAndUserAccount_Username(Long id, String username);

  Page<Letter> findByTitleContaining(String title, Pageable pageable);

  Page<Letter> findByUserAccount_NicknameContaining(String content, Pageable pageable);

  @Override
  default void customize(QuerydslBindings bindings, QLetter root) {
    // 리스트에 추가되지 않는 프로퍼티는 바인딩 제외하기
    bindings.excludeUnlistedProperties(true);

    // 바인딩 리스트에 추가하기
    bindings.including(root.userAccount.nickname, root.title);

    // 바인딩된 필드에 대한 조건 설정
    bindings.bind(root.userAccount.nickname).first(StringExpression::containsIgnoreCase);
    bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
  }
}
