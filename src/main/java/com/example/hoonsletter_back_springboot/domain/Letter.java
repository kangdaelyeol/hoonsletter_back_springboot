package com.example.hoonsletter_back_springboot.domain;

import com.example.hoonsletter_back_springboot.domain.constant.LetterType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


@Getter
@Setter
@Table(indexes = {
    @Index(columnList = "user_id"),
    @Index(columnList = "createdAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
@ToString
public class Letter {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 50)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private LetterType type;

  private String thumbnailUrl;

  @CreatedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private boolean updatable;

  @ToString.Exclude
  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private UserAccount userAccount;


  @ToString.Exclude
  @OneToMany(mappedBy = "letter", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LetterScene> letterScenes = new ArrayList<>();

  public void addLetterScene(LetterScene scene){
    letterScenes.add(scene);
    scene.setLetter(this);
  }

  public void removeLetterScene(LetterScene scene){
    letterScenes.remove(scene);
    scene.setLetter(this);
  }

  protected Letter(){} // no-args constructor

  private Letter(String title, LetterType letterType, boolean updatable, String thumbnailUrl, UserAccount userAccount){
    this.title = title;
    this.type = letterType;
    this.thumbnailUrl = thumbnailUrl;
    this.updatable = updatable;
    this.userAccount = userAccount;
  }

  public static Letter of(String title, LetterType letterType, boolean updatable, String thumbnailUrl, UserAccount userAccount){
    return new Letter(title, letterType, updatable, thumbnailUrl, userAccount);
  }

  public static Letter of(String title, LetterType letterType, String thumbnailUrl, UserAccount userAccount){
    return Letter.of(title, letterType, true, thumbnailUrl, userAccount);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof Letter that)) return false;

    return getId() != null && getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }
}
