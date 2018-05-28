package com.nagygm.collaboard.user.admin.usermanager.service;

import com.nagygm.collaboard.user.admin.usermanager.web.AppUserDto;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface AppUserManagerService {
  
  AppUserDto getUser(String username);
  List<AppUserDto> getUsers();
  Optional<Boolean> toggleActivation(String username);
  Optional<Boolean> delete(String username);
}
