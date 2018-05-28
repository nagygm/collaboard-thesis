package com.nagygm.collaboard.whiteboard.domain;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
  @Type(value = FabricCreateCommand.class, name = "fabric_create"),
  @Type(value = FabricDeleteCommand.class, name = "fabric_delete"),
  @Type(value = FabricUpdateCommand.class, name = "fabric_update"),
  @Type(value = FabricSelectCommand.class, name = "fabric_select"),
  @Type(value = FabricDeselectCommand.class, name = "fabric_deselect"),
  @Type(value = FailCommand.class, name = "fail")
})
public interface Command {
  String getId();
}
