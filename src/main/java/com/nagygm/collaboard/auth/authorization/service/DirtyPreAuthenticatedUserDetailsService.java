package com.nagygm.collaboard.auth.authorization.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DirtyPreAuthenticatedUserDetailsService extends
  PreAuthenticatedGrantedAuthoritiesUserDetailsService {
  final
  UserDetailsService userDetailsService;
  
  @Autowired
  public DirtyPreAuthenticatedUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
  
  protected UserDetails createUserDetails(Authentication token,
    Collection<? extends GrantedAuthority> authorities) {
    return userDetailsService.loadUserByUsername(token.getName());
  }
}
