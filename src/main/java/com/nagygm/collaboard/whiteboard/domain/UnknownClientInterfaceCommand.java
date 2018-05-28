package com.nagygm.collaboard.whiteboard.domain;



public class UnknownClientInterfaceCommand extends FailCommand{
  public UnknownClientInterfaceCommand() {
    message = "Unknown Client Interface";
  }
}
