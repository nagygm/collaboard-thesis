package com.nagygm.collaboard.auth.authorization.service;

import com.nagygm.collaboard.auth.authorization.dal.RoleRepository;
import com.nagygm.collaboard.auth.authorization.domain.Role;
import com.nagygm.collaboard.auth.authorization.domain.Role.Values;
import com.nagygm.collaboard.user.domain.AppUser;
import java.util.Collections;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
  final
  RoleRepository roleRepository;
  
  @Autowired
  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }
  
  @Override
  public AppUser assignRoleWithoutSave(AppUser appUser, Values role) {
    Role userRole = roleRepository.findByName(role.code);
    appUser.setRoles(new HashSet<>(Collections.singletonList(userRole)));
    return appUser;
  }
}
