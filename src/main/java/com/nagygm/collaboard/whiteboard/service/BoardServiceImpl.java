package com.nagygm.collaboard.whiteboard.service;

import com.nagygm.collaboard.auth.authorization.dal.UserBoardDetailsAuthorityRepository;
import com.nagygm.collaboard.boardmanager.dal.BoardDetailsRepository;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsDto;
import com.nagygm.collaboard.whiteboard.domain.BoardData;
import com.nagygm.collaboard.whiteboard.dal.BoardDataRepository;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.whiteboard.domain.Command;
import com.nagygm.collaboard.whiteboard.domain.UnknownClientInterfaceCommand;
import java.math.BigInteger;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {
  
  final private BoardDataRepository boardDataRepository;
  final private BoardDetailsRepository boardDetailsRepository;
  final private UserBoardDetailsAuthorityRepository userBoardDetailsAuthorityRepository;
  final private CommandExecutorServiceManager commandExecutorServiceManager;
  final private ModelMapper modelMapper;
  
  @Autowired
  public BoardServiceImpl(BoardDataRepository boardDataRepository,
    BoardDetailsRepository boardDetailsRepository,
    UserBoardDetailsAuthorityRepository userBoardDetailsAuthorityRepository,
    CommandExecutorServiceManager commandExecutorServiceManager,
    ModelMapper modelMapper) {
    this.boardDataRepository = boardDataRepository;
    this.boardDetailsRepository = boardDetailsRepository;
    this.userBoardDetailsAuthorityRepository = userBoardDetailsAuthorityRepository;
    this.commandExecutorServiceManager = commandExecutorServiceManager;
    this.modelMapper = modelMapper;
  }
  
  @Override
  public Command translateCommand(Command command, String urlHash) {
    BoardDetails boardDetails = boardDetailsRepository.findByUrlHash(urlHash).get();
    return commandExecutorServiceManager.provideExecutorService(command).flatMap(
      it -> Optional.of(it.execute(command, BigInteger.valueOf(boardDetails.getId())))
    ).orElse(new UnknownClientInterfaceCommand());
  }
  
  
  /**
   * Returns a specific Board with populated BoardData
   *
   * @param urlHash urlHash contained in the board url
   */
  @Override
  public BoardDetailsDto getBoardDetails(String urlHash) {
    return modelMapper.map(
      boardDetailsRepository.findByUrlHash(urlHash).get(),
      BoardDetailsDto.class
    );
  }
  
  /**
   * Returns a specific Board with populated BoardData
   *
   * @param urlHash urlHash contained in the board url
   */
  @Override
  public Optional<BoardData> getBoardData(String urlHash) {
    return boardDetailsRepository.findByUrlHash(urlHash).flatMap(it -> {
      return boardDataRepository.findById(BigInteger.valueOf(it.getId()));
    });
  }
}
