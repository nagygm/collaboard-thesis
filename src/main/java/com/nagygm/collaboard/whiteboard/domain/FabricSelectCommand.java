package com.nagygm.collaboard.whiteboard.domain;

import lombok.Data;

@Data
public class FabricSelectCommand implements SelectCommand, FabricCommand{
  String objectId;
  private String id;
}
