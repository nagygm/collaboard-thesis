package com.nagygm.collaboard.boardmanager.web;

import com.nagygm.collaboard.auth.authorization.domain.BoardRoleValues;
import com.nagygm.collaboard.common.web.BaseDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoardDetailsWithPermissionDto extends BaseDto {
  private String title;
  private String description;
  private String urlHash;
  private BoardRoleValues permission;
}
