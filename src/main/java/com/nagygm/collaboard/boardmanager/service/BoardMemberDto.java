package com.nagygm.collaboard.boardmanager.service;

import com.nagygm.collaboard.auth.authorization.domain.BoardRoleValues;
import com.nagygm.collaboard.common.web.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BoardMemberDto extends BaseDto {
  private String username;
  private String firstName;
  private String lastName;
  private BoardRoleValues permission;
}
