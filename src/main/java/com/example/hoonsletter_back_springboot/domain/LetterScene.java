package com.example.hoonsletter_back_springboot.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class LetterScene {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private int partOrder;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "letter_id")
  private Letter letter;

  @ToString.Exclude
  @OneToMany(mappedBy = "letterScene", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SceneMessage> sceneMessages;

  @ToString.Exclude
  @OneToMany(mappedBy = "letterScene", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ScenePicture> scenePictures;

  protected LetterScene(){} // no-args constructor

  private LetterScene(int partOrder, Letter letter){
    this.partOrder = partOrder;
    this.letter = letter;
  }

  public static LetterScene of(int partOrder, Letter letter){
    return new LetterScene(partOrder, letter);
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(!(o instanceof LetterScene that)) return false;

    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
