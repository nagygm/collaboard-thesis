package com.nagygm.collaboard.boardmanager.web;

import com.nagygm.collaboard.common.web.BaseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardPreferencesDto extends BaseDto {
  private String title;
  private String description;
  private String urlHash;
  
  
  
}
