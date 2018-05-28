package com.nagygm.collaboard.auth.authorization.domain;

import com.nagygm.collaboard.user.domain.AppUser;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true)
  private String name;
  @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Collection<AppUser> appUsers;
  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(name = "roles_authorities",
    joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
  )
  private Collection<Authority> authorities;
  
  
  public static enum Values {
    USER(Const.ROLE_USER), ADMIN(Const.ROLE_ADMIN);
    
    public final String code;
    
    private Values(String code) {
      this.code = code;
    }
  
    public static class Const {
      public static final String ROLE_USER = "ROLE_USER";
      public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
  }
}
