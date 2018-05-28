package com.nagygm.collaboard.whiteboard.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeName("fail")
public class FailCommand implements Command{
  private String id;
  protected String message;
}
