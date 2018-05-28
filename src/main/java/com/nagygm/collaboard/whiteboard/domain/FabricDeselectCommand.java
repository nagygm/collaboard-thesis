package com.nagygm.collaboard.whiteboard.domain;

import lombok.Data;

@Data
public class FabricDeselectCommand implements DeselectCommand, FabricCommand {
  private String objectId;
  private String id;
}
