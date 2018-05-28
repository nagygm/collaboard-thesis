package com.nagygm.collaboard.user.admin.usermanager.web;

import com.nagygm.collaboard.common.web.BaseDto;
import com.nagygm.collaboard.user.domain.AppUser;
import lombok.Data;

@Data
public class AppUserDto extends BaseDto {
  private boolean active;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  
  public AppUserDto(AppUser appUser) {
    this.active = appUser.isActive();
    this.username = appUser.getUsername();
    this.firstName = appUser.getFirstName();
    this.lastName = appUser.getLastName();
    this.email = appUser.getEmail();
  }
  
}
