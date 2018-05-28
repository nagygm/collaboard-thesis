package com.nagygm.collaboard.whiteboard.service;

import com.nagygm.collaboard.boardmanager.web.BoardDetailsDto;
import com.nagygm.collaboard.whiteboard.domain.BoardData;
import com.nagygm.collaboard.whiteboard.domain.BoardObject;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.whiteboard.domain.Command;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface BoardService {
  
  Command translateCommand(Command command, String urlHash);
  @PreAuthorize("hasAuthority('BOARD_READ_' + #urlHash)")
  BoardDetailsDto getBoardDetails(String urlHash);
  
  @PreAuthorize("hasAuthority('BOARD_READ_' + #urlHash)")
  Optional<BoardData> getBoardData(String urlHash);
}
