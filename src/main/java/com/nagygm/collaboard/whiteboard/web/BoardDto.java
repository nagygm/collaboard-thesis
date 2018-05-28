package com.nagygm.collaboard.whiteboard.web;

import com.nagygm.collaboard.common.web.BaseDto;
import com.nagygm.collaboard.whiteboard.domain.BoardObject;
import java.util.Collection;
import lombok.Data;

@Data
public class BoardDto extends BaseDto {
  private String urlHash;
  private Collection<BoardObject> objects;
  private String version;
  private String drawingLibrary;
}
