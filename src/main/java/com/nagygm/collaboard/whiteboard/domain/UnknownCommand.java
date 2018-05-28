package com.nagygm.collaboard.whiteboard.domain;

public class UnknownCommand extends FailCommand{
  public UnknownCommand() {
    message = "Unknown Command";
  }
}
