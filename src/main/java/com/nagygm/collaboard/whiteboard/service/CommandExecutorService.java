package com.nagygm.collaboard.whiteboard.service;

import com.nagygm.collaboard.whiteboard.domain.Command;
import java.math.BigInteger;

public interface CommandExecutorService {
  Command execute(Command cmd, BigInteger id);
  boolean supports(Class<?> clazz);
}
