package com.nagygm.collaboard.boardmanager.web;

import com.nagygm.collaboard.common.web.BaseDto;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

@Data
public class CreateBoardDto extends BaseDto {
  @NotEmpty
  @Length(min = 3, max = 100)
  private String title;
  @Length(max= 300)
  private String description;
  @Transient
  private String boardType;
  @Transient
  private String version;
  @Transient
  private Boolean anonymusAllowedToRead;
  @Transient
  private String drawingLibrary;
}
