package com.example.hoonsletter_back_springboot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Entity
@ToString
public class LetterMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int order;

  @Column(nullable = false)
  private String content;

  protected LetterMessage() {} // no-args constructor

  private LetterMessage(int order, String content){
    this.order = order;
    this.content = content;
  }

  public static LetterMessage of(int order, String content){
    return new LetterMessage(order, content);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof LetterMessage that)) return false;

    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
