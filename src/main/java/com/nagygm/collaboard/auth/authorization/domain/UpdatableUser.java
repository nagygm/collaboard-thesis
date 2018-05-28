package com.nagygm.collaboard.auth.authorization.domain;


import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UpdatableUser implements UpdatableUserDetails {
  private boolean dirty = false;
  
  Collection<? extends GrantedAuthority> grantedAuthorities;
  private final UserDetails delegate;
  
  public UpdatableUser(UserDetails user) {
    this.delegate = user;
    this.grantedAuthorities = user.getAuthorities();
  }
  
  @Override
  public String getPassword() {
    return delegate.getPassword();
  }
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }
  
  @Override
  public String getUsername() {
    return delegate.getUsername();
  }
  
  @Override
  public boolean isAccountNonExpired() {
    return delegate.isAccountNonExpired();
  }
  
  @Override
  public boolean isAccountNonLocked() {
    return delegate.isAccountNonLocked();
  }
  
  @Override
  public boolean isCredentialsNonExpired() {
    return delegate.isCredentialsNonExpired();
  }
  
  @Override
  public boolean isEnabled() {
    return delegate.isEnabled();
  }
  
  @Override
  public void setGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
    this.grantedAuthorities = grantedAuthorities;
  }
  
  @Override
  public boolean equals(Object rhs) {
    if (rhs instanceof UpdatableUser) {
      return delegate.getUsername().equals(((UpdatableUser) rhs).getUsername());
    }
    return false;
  }
  
  /**
   * Returns the hashcode of the {@code username}.
   */
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }
  
  @Override
  public boolean isDirty() {
    return dirty;
  }
  
  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
  
}
