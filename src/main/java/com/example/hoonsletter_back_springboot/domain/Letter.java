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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private boolean updatable;

  @ToString.Exclude
  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;


  // Letter -> LetterMessage 단방향 매핑
  @ToString.Exclude
  @JoinColumn(name = "letter_id")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<LetterMessage> letterMessages;

  // Letter -> LetterPicture 단방향 매핑
  @ToString.Exclude
  @JoinColumn(name = "letter_id")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<LetterPicture> letterPictures;
}
