package com.example.hoonsletter_back_springboot.domain;

import com.example.hoonsletter_back_springboot.domain.constant.RoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(columnList = "username")
})
@ToString
public class UserAccount implements UserDetails {
  @Id
  @Column(length = 50, updatable = false, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(length = 50, nullable = false, unique = true)
  private String nickname;

  @Column(nullable = false)
  private String profileUrl;

  @ToString.Exclude
  @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Letter> letterList;

  protected UserAccount(){}

  private UserAccount(String username, String password, String nickname, String profileUrl, List<RoleType> authorities) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.profileUrl = profileUrl;
    this.authorities = authorities;
  }

  public static UserAccount of(String username, String password, String nickname, String profileUrl){
    return new UserAccount(username, password, nickname, profileUrl, List.of(RoleType.ROLE_USER)); // default값으로 ROLE_USER를 준다.
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

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_username"))
  @Column(name = "authority")
  @Enumerated(EnumType.STRING)
  private List<RoleType> authorities = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities.stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .toList();
  }
}
