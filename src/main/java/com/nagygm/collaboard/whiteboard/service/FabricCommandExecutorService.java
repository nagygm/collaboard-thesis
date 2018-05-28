package com.nagygm.collaboard.whiteboard.service;

import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import com.nagygm.collaboard.whiteboard.dal.BoardDataRepository;
import com.nagygm.collaboard.whiteboard.dal.FabricBoardObjectDao;
import com.nagygm.collaboard.whiteboard.domain.Command;
import com.nagygm.collaboard.whiteboard.domain.CreateCommand;
import com.nagygm.collaboard.whiteboard.domain.DeleteCommand;
import com.nagygm.collaboard.whiteboard.domain.DeselectCommand;
import com.nagygm.collaboard.whiteboard.domain.FabricBoardObject;
import com.nagygm.collaboard.whiteboard.domain.FabricCommand;
import com.nagygm.collaboard.whiteboard.domain.FabricCreateCommand;
import com.nagygm.collaboard.whiteboard.domain.FabricDeleteCommand;
import com.nagygm.collaboard.whiteboard.domain.FabricDeselectCommand;
import com.nagygm.collaboard.whiteboard.domain.FabricSelectCommand;
import com.nagygm.collaboard.whiteboard.domain.FabricUpdateCommand;
import com.nagygm.collaboard.whiteboard.domain.FailCommand;
import com.nagygm.collaboard.whiteboard.domain.SelectCommand;
import com.nagygm.collaboard.whiteboard.domain.UnknownCommand;
import com.nagygm.collaboard.whiteboard.domain.UpdateCommand;
import java.math.BigInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FabricCommandExecutorService implements CommandExecutorService {
  
  private static Logger log = LogManager.getLogger(FabricCommandExecutorService.class);
  
  final private BoardDataRepository boardDataRepository;
  final private FabricBoardObjectDao fabricBoardObjectDao;
  final private ModelMapper modelMapper;
  
  @Autowired
  public FabricCommandExecutorService(
    BoardDataRepository boardDataRepository,
    FabricBoardObjectDao fabricBoardObjectDao, ModelMapper modelMapper) {
    this.boardDataRepository = boardDataRepository;
    this.fabricBoardObjectDao = fabricBoardObjectDao;
    this.modelMapper = modelMapper;
  }
  
  @Override
  public Command execute(Command cmd, BigInteger id) {
    //TODO more proper
    try {
      if (CreateCommand.class.isAssignableFrom(cmd.getClass())) {
        return executeCreate((FabricCreateCommand) cmd, id);
      } else if (DeleteCommand.class.isAssignableFrom(cmd.getClass())) {
        return executeDelete((FabricDeleteCommand) cmd, id);
      } else if (UpdateCommand.class.isAssignableFrom(cmd.getClass())) {
        return executeUpdate((FabricUpdateCommand) cmd, id);
      } else if (FabricSelectCommand.class.isAssignableFrom(cmd.getClass())) {
        return executeSelect((FabricSelectCommand)cmd, id);
      } else if (FabricDeselectCommand.class.isAssignableFrom(cmd.getClass())) {
        return executeDeselect((FabricDeselectCommand)cmd, id);
      } else {
        return new UnknownCommand();
      }
    } catch (MongoException e) {
      log.debug(e);
      return new FailCommand(cmd.getId(), "failed to save boardobject");
    }
  }
  
  @Override
  public boolean supports(Class<?> clazz) {
    return FabricCommand.class.isAssignableFrom(clazz);
  }
  
  private Command executeCreate(FabricCreateCommand command, BigInteger id) {
    FabricBoardObject boardObject = (command).getBoardObject();
    boardObject.setId(ObjectId.get().toHexString());
    UpdateResult result = fabricBoardObjectDao.saveBoardObject(id, boardObject);
    if (result.getModifiedCount() > 0) {
      FabricCreateCommand cmd = new FabricCreateCommand();
      cmd.setId(command.getId());
      cmd.setBoardObject(boardObject);
      return cmd;
    } else {
      return new FailCommand(command.getId(), "Failed to save object");
    }
  }
  
  private Command executeDelete(FabricDeleteCommand command, BigInteger id) {
    String objectId = command.getObjectId();
    fabricBoardObjectDao.deleteBoardObject(id, objectId);
    return command;
  }
  
  private Command executeUpdate(FabricUpdateCommand command, BigInteger id) {
    FabricBoardObject boardObject = command.getBoardObject();
    fabricBoardObjectDao.updateBoardObject(id, boardObject);
    FabricUpdateCommand cmd = new FabricUpdateCommand();
    cmd.setId(command.getId());
    cmd.setBoardObject(boardObject);
    return cmd;
  }
  
  private Command executeSelect(FabricSelectCommand command, BigInteger id) {
    //TODO WLOCK
    return command;
  }
  
  private Command executeDeselect(FabricDeselectCommand command, BigInteger id) {
    //TODO WLOCK
    return command;
  }
}
