package com.nagygm.collaboard.user.service;

import com.nagygm.collaboard.auth.authorization.domain.Role.Values;
import com.nagygm.collaboard.user.domain.AppUser;
import com.nagygm.collaboard.user.profile.web.RegistrationDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {
  Optional<AppUser> findUserByUserName(String username);
  AppUser saveUser(AppUser appUser, Values role);
  RegistrationDto registerUser(RegistrationDto registrationDto);
}


