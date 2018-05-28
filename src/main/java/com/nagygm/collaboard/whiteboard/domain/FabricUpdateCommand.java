package com.nagygm.collaboard.whiteboard.domain;

import com.nagygm.collaboard.whiteboard.domain.FabricBoardObject;
import com.nagygm.collaboard.whiteboard.domain.FabricCommand;
import com.nagygm.collaboard.whiteboard.domain.UpdateCommand;
import lombok.Data;

@Data
public class FabricUpdateCommand implements UpdateCommand, FabricCommand {
  private FabricBoardObject boardObject;
  private String id;
}
