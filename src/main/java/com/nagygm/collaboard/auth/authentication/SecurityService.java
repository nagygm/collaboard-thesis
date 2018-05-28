package com.nagygm.collaboard.auth.authentication;

import com.nagygm.collaboard.auth.authorization.domain.UpdatableUser;
import com.nagygm.collaboard.auth.authorization.domain.UpdatableUserDetails;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityService {
  
  private final AuthenticationManager authenticationManager;
  
  private final UserDetailsService userDetailsService;
  private final SessionRegistry sessionRegistry;
  private static final Logger logger = LogManager.getLogger(SecurityService.class);
  
  @Autowired
  public SecurityService(AuthenticationManager authenticationManager,
    UserDetailsService userDetailsService,
    SessionRegistry sessionRegistry) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.sessionRegistry = sessionRegistry;
  }
  
  public String findLoggedInUsername() {
    Principal principal = SecurityContextHolder.getContext().getAuthentication();
    return principal.getName();
  }
  
  public boolean hasAuthority(String authority) {
    UpdatableUser principal = (UpdatableUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return principal.getAuthorities().contains(new SimpleGrantedAuthority(authority));
  }
  
  @Transactional
  public void updateGrantedAuthoritesForUser(String username) {
    Optional<Object> principalOptional = sessionRegistry.getAllPrincipals().stream()
      .filter(p -> ((UserDetails) p).getUsername().equals(username)).findFirst();
    if (principalOptional.isPresent()) {
      Optional<SessionInformation> sessionInformation = sessionRegistry
        .getAllSessions(principalOptional.get(), false).stream().findFirst();
      
      sessionInformation.ifPresent(it -> {
          UpdatableUserDetails userDetails = ((UpdatableUserDetails) it.getPrincipal());
          userDetails.setGrantedAuthorities(
            userDetailsService.loadUserByUsername(username).getAuthorities());
          sessionInformation.get().expireNow();
          userDetails.setDirty(true);
          //TODO proper session transfer
          sessionRegistry.registerNewSession(it.getSessionId(), userDetails);
        }
      );
    }
  }
  
  @Transactional
  public void updateGrantedAuthoritesForSelf() {
    updateGrantedAuthoritesForUser(findLoggedInUsername());
  }
}
