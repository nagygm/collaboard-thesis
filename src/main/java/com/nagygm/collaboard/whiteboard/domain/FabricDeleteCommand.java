package com.nagygm.collaboard.whiteboard.domain;

import lombok.Data;

@Data
public class FabricDeleteCommand implements DeleteCommand, FabricCommand{
  private String objectId;
  private String id;
}
