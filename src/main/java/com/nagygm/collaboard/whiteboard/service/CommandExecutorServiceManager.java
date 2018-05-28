package com.nagygm.collaboard.whiteboard.service;

import com.nagygm.collaboard.whiteboard.domain.Command;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutorServiceManager {
  
  final List<CommandExecutorService> list;
  
  @Autowired
  public CommandExecutorServiceManager(List<CommandExecutorService> list) {
    this.list = list;
  }
  
  public Optional<CommandExecutorService> provideExecutorService(Command cmd) {
    return list.stream().filter(it -> it.supports(cmd.getClass())).findFirst();
  }
}
