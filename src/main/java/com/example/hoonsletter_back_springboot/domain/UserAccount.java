package com.example.hoonsletter_back_springboot.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
public class UserAccount {
  @Id
  @Column(length = 50, updatable = false, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(length = 50, nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String profileUrl;

  @ToString.Exclude
  @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Letter> letters;

  protected UserAccount(){}

  private UserAccount(String username, String password, String nickname, String profileUrl) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.profileUrl = profileUrl;
  }

  public static UserAccount of(String username, String password, String nickname, String profileUrl){
    return new UserAccount(username, password, nickname, profileUrl);
  }

  @Override
  public boolean equals(Object o){
    if(this == o) return true;

    if(!(o instanceof UserAccount that)) return false;

    return getUsername() != null && getUsername().equals(that.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername());
  }
}
