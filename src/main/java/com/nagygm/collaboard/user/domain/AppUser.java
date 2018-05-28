package com.nagygm.collaboard.user.domain;

import com.nagygm.collaboard.auth.authorization.domain.Role;
import com.nagygm.collaboard.auth.authorization.domain.UserBoardDetailsAuthority;
import com.nagygm.collaboard.common.domain.BaseDomain;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@Entity
@Table(name = "app_user")
public class AppUser extends BaseDomain {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "email")
  @Email
  private String email;
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  @Transient
  private String password;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "active")
  private boolean active;
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
    name = "app_user_role",
    joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id")
  )
  private Set<Role> roles;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "appUser")
  private Set<UserBoardDetailsAuthority> userBoardDetailsAuthorities;
  
}
