package com.nagygm.collaboard.auth.authorization.service;

import com.nagygm.collaboard.auth.authorization.domain.Role.Values;
import com.nagygm.collaboard.user.domain.AppUser;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
  AppUser assignRoleWithoutSave(AppUser appUser, Values role);
}
