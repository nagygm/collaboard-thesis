package com.nagygm.collaboard.auth.authorization.domain;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class BoardRole {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;
  String name;
//  @ManyToMany(cascade = CascadeType.PERSIST)
//  @JoinTable(name = "roles_authorities",
//    joinColumns = @JoinColumn(name = "board_role_id", referencedColumnName = "id"),
//    inverseJoinColumns = @JoinColumn(name = "board_authority_id", referencedColumnName = "id")
//  )
//  Collection<BoardAuthority> boardAuthorities;
  
}
