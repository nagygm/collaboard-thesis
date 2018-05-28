package com.nagygm.collaboard.auth.authorization.domain;

import java.util.Collection;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity(name = "board_authority")
public class BoardAuthority {
  @Id
  private Integer id;
  private String name;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "boardAuthority")
  private Set<UserBoardDetailsAuthority> userBoardDetailsAuthorities;
//  @ManyToMany(mappedBy = "boardAuthorities")
//  private Collection<BoardRole> boardRoles;
}
