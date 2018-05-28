package com.nagygm.collaboard.boardmanager.service;

public class InconsistentBoardDatabaseException extends RuntimeException {
  
  public InconsistentBoardDatabaseException() {
  }
  
  public InconsistentBoardDatabaseException(String message) {
    super(message);
  }
  
  public InconsistentBoardDatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public InconsistentBoardDatabaseException(Throwable cause) {
    super(cause);
  }
  
  public InconsistentBoardDatabaseException(String message, Throwable cause,
    boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
