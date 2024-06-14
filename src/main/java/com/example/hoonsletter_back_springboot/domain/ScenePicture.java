package com.example.hoonsletter_back_springboot.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class ScenePicture {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int order;

  @Column(nullable = false)
  private String url;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "scene_id")
  private LetterScene letterScene;

  protected ScenePicture(){}

  private ScenePicture(int order, String url, LetterScene letterScene){
    this.order = order;
    this.url = url;
    this.letterScene = letterScene;
  }

  public static ScenePicture of(int order, String url, LetterScene letterScene){
    return new ScenePicture(order, url, letterScene);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof ScenePicture that)) return false;

    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode(){
    return Objects.hash(getId());
  }
}
