package com.nagygm.collaboard.user.service;

import com.nagygm.collaboard.auth.authorization.domain.Role.Values;
import com.nagygm.collaboard.auth.authorization.service.RoleService;
import com.nagygm.collaboard.user.dal.AppUserRepository;
import com.nagygm.collaboard.user.domain.AppUser;
import com.nagygm.collaboard.user.profile.web.RegistrationDto;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService {
  private final AppUserRepository appUserRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ModelMapper modelMapper;
  private RoleService roleService;
  
  @Autowired
  public AppUserServiceImpl(AppUserRepository appUserRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper,
    RoleService roleService) {
    this.appUserRepository = appUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.modelMapper = modelMapper;
    this.roleService = roleService;
  }
  
  @Override
  public Optional<AppUser> findUserByUserName(String username) {
    return appUserRepository.findByUsername(username);
  }
  
  @Override
  public AppUser saveUser(AppUser appUser, Values role) {
    appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
    appUser.setActive(true);
    appUser = roleService.assignRoleWithoutSave(appUser, role);
    return appUserRepository.save(appUser);
  }
  
  @Transactional
  @Override
  public RegistrationDto registerUser(RegistrationDto dto) {
    //TODO move to Validator class
    
    if(dto.getErrors().hasErrors()) {
      return dto;
    }
    List<AppUser> userList =
      appUserRepository.findAllByEmailOrUsername(dto.getEmail(), dto.getUsername());
    if(userList.stream().anyMatch(it -> it.getEmail().equals(dto.getEmail()))) {
      dto.getErrors().rejectValue("email", "registration.error.email.taken");
    }
    if(userList.stream().anyMatch(it -> it.getUsername().equals(dto.getUsername()))) {
      dto.getErrors().rejectValue("username", "registration.error.username.taken");
    }
    if(dto.getUsername().contains("#")) {
      dto.getErrors().rejectValue("username", "registration.error.invalidChar.hashTag");
    }
    if(!dto.getPassword().equals(dto.getPasswordConfirmation())) {
      dto.getErrors().rejectValue("passwordConfirmation", "registration.error.password.notTheSame");
    }
    if(!dto.getErrors().hasErrors()) {
      AppUser appUser = modelMapper.map(dto, AppUser.class);
      saveUser(appUser, Values.USER);
    }
    
    return dto;
  }
  
}
