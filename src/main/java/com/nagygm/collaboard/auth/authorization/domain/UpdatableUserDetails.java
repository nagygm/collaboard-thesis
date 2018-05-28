package com.nagygm.collaboard.auth.authorization.domain;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface UpdatableUserDetails extends UserDetails {
  
  void setGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities);
  
  boolean isDirty();
  
  void setDirty(boolean dirty);
}
