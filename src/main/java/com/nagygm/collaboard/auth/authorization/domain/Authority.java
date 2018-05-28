package com.nagygm.collaboard.auth.authorization.domain;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity(name = "authority")
public class Authority {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true, name = "name")
  private String name;
  @ManyToMany(mappedBy = "authorities")
  private Collection<Role> roles;
}
