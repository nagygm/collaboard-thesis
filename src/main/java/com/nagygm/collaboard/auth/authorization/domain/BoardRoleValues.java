package com.nagygm.collaboard.auth.authorization.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BoardRoleValues {
  BOARD_READ(1), BOARD_WRITE(2), BOARD_ADMIN(3), BOARD_OWNER(4);
  
  public final int level;
  
  BoardRoleValues(int level) {
    this.level = level;
  }
  
  public String withUrl(String url) {
    return this.name() + "_" + url;
  }
  
  public List<Integer> getAllforRole() {
    return Arrays.stream(BoardRoleValues.values()).filter(it -> it.level <= this.level)
      .map(it -> it.level)
      .collect(Collectors.toList());
  }
  
  public List<BoardRoleValues> getAllBoardValuesforRole() {
    return Arrays.stream(BoardRoleValues.values()).filter(it -> it.level <= this.level)
      .collect(Collectors.toList());
  }
  
}
