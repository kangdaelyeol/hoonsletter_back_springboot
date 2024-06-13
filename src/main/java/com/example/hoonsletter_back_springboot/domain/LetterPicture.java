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
public class LetterPicture {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int order;

  @Column(nullable = false)
  private String url;

  protected LetterPicture(){}

  private LetterPicture(int order, String url){
    this.order = order;
    this.url = url;
  }

  public static LetterPicture of(int order, String url){
    return new LetterPicture(order, url);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof LetterPicture that)) return false;

    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode(){
    return Objects.hash(getId());
  }
}
