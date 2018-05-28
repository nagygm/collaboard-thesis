package com.nagygm.collaboard.auth.authorization.service;

import com.nagygm.collaboard.auth.authorization.dal.UserBoardDetailsAuthorityRepository;
import com.nagygm.collaboard.auth.authorization.domain.Authority;
import com.nagygm.collaboard.auth.authorization.domain.Role;
import com.nagygm.collaboard.auth.authorization.domain.UpdatableUser;
import com.nagygm.collaboard.user.dal.AppUserRepository;
import com.nagygm.collaboard.user.domain.AppUser;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom implementation to manage resource authorities without acl
 */
@Service("userDetailsService")
public class CollaboardUserDetailsService implements UserDetailsService {
  
  private final AppUserRepository appUserRepository;
  private final UserBoardDetailsAuthorityRepository userBoardAuthorityRepository;
  
  @Autowired
  public CollaboardUserDetailsService(AppUserRepository appUserRepository,
    UserBoardDetailsAuthorityRepository userBoardAuthorityRepository) {
    this.appUserRepository = appUserRepository;
    this.userBoardAuthorityRepository = userBoardAuthorityRepository;
  }
  
  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) {
    Optional<AppUser> appUser = appUserRepository.findByUsername(username);
    UserDetails userDetails = appUser.flatMap(it ->
        Optional.of(
          new UpdatableUser(new User(
            it.getUsername(),
            it.getPassword(),
            it.isActive(),
            true,
            true,
            true,
            mergeAuthorities(it)
          ))
        )
      ).orElseThrow(() -> new UsernameNotFoundException("UserName not found in our database"));
      return userDetails;
  }
  
  private Collection<? extends GrantedAuthority> mergeAuthorities(AppUser appUser) {
    return mapToGrantedAuthorities(getAllAuthorities(appUser));
  }
  
  private List<String> getAllAuthorities(AppUser appUser) {
    List<String> allAuthoroties = appUser.getRoles().stream().map(Role::getName)
      .collect(Collectors.toList());
    
    appUser.getRoles().forEach(
      it -> allAuthoroties.addAll(
        it.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList())
      )
    );
    
    allAuthoroties.addAll(
      userBoardAuthorityRepository.
        findByAppUser(appUser)
        .stream()
        .map(
          it -> it.getBoardAuthority().getName()+ "_" + it.getBoardDetails().getUrlHash()
        ).collect(Collectors.toList()));
    return allAuthoroties;
  }
  
  private List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
    return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }
}
