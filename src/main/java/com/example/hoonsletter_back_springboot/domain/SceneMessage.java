package com.example.hoonsletter_back_springboot.domain;

import com.example.hoonsletter_back_springboot.domain.constant.MessageColorType;
import com.example.hoonsletter_back_springboot.domain.constant.MessageSizeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class SceneMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int order;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MessageSizeType sizeType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private MessageColorType colorType;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "scene_id")
  private LetterScene letterScene;

  protected SceneMessage() {} // no-args constructor

  private SceneMessage(int order, String content, MessageSizeType sizeType, MessageColorType colorType){
    this.order = order;
    this.content = content;
    this.sizeType = sizeType;
    this.colorType = colorType;
  }

  public static SceneMessage of(int order, String content, MessageSizeType sizeType, MessageColorType colorType){
    return new SceneMessage(order, content, sizeType, colorType);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof SceneMessage that)) return false;

    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
