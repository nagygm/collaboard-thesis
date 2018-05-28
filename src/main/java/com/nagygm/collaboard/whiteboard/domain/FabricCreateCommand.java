package com.nagygm.collaboard.whiteboard.domain;

import lombok.Data;

@Data
public class FabricCreateCommand implements CreateCommand, FabricCommand{
  private FabricBoardObject boardObject;
  private String id;
}
