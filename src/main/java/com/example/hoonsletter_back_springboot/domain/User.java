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
import java.util.Objects;
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

  protected User(){}

  private User(String username, String password, String nickname, String profileUrl) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.profileUrl = profileUrl;
  }

  public static User of(String username, String password, String nickname, String profileUrl){
    return new User(username, password, nickname, profileUrl);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof User that)) return false;

    return getUsername() != null && getUsername().equals(that.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername());
  }
}
