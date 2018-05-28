package com.nagygm.collaboard.user.profile.web;

import com.nagygm.collaboard.common.web.BaseDto;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegistrationDto extends BaseDto {
  @Email
  @Length(min = 5, max = 100)
  @NotEmpty
  private String email;
  @NotEmpty
  @Length(min = 5, max = 20)
  private String username;
  @Length(min = 8, max = 100)
  private String password;
  @Length(min = 8, max = 100)
  private String passwordConfirmation;
  @Length(min = 1, max = 100)
  @NotEmpty
  private String firstName;
  @Length(max = 100)
  private String lastName;
}
