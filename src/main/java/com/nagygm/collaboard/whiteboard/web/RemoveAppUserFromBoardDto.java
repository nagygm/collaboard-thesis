package com.nagygm.collaboard.whiteboard.web;

import com.nagygm.collaboard.common.web.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RemoveAppUserFromBoardDto extends BaseDto {
  private String username;
  private String boardUrl;
}
