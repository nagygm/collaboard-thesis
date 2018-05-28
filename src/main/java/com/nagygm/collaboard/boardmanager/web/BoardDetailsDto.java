package com.nagygm.collaboard.boardmanager.web;

import com.nagygm.collaboard.common.web.BaseDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class BoardDetailsDto extends BaseDto {
  @Length(min = 2, max = 100)
  private String title;
  @Length(min = 1, max = 300)
  private String description;
  private String urlHash;
}
