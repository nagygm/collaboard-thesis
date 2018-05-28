package com.nagygm.collaboard.user.admin.usermanager.service;

import com.nagygm.collaboard.user.admin.usermanager.web.AppUserDto;
import com.nagygm.collaboard.user.dal.AppUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AppUserManagerServiceImpl implements AppUserManagerService {
  final AppUserRepository appUserRepository;
  
  @Autowired
  public AppUserManagerServiceImpl(@Qualifier("appUserRepository") AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }
  
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Override
  public AppUserDto getUser(String username) {
    return appUserRepository.findByUsername(username)
      .flatMap(it -> Optional.of(new AppUserDto(it)))
      .orElse(null) ;
  }
  
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Override
  public List<AppUserDto> getUsers() {
    List<AppUserDto> users = new ArrayList<>();
    appUserRepository.findAll().forEach(it -> users.add(new AppUserDto(it)));
    return users;
  }
  
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Override
  public Optional<Boolean> toggleActivation(String username) {
    return appUserRepository.findByUsername(username).flatMap(it -> {
      it.setActive(!it.isActive());
      appUserRepository.save(it);
      return Optional.of(true);
    });
  }
  
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @Override
  public Optional<Boolean> delete(String username) {
    return appUserRepository.findByUsername(username).flatMap(it -> {
      appUserRepository.delete(it);
      return Optional.of(true);
    });
  }
}
