package com.example.hoonsletter_back_springboot.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(columnList = "username")
})
@ToString
public class User {
  @Id
  @Column(length = 50, updatable = false, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(length = 50, nullable = false)
  private String nickname;

  @Column
  private String profileUrl;

  @ToString.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Letter> letters;

}
