package com.nagygm.collaboard.boardmanager.service;

import com.nagygm.collaboard.auth.authorization.domain.Role.Values.Const;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsDto;
import com.nagygm.collaboard.boardmanager.web.BoardDetailsWithPermissionDto;
import com.nagygm.collaboard.boardmanager.web.CreateBoardDto;
import com.nagygm.collaboard.boardmanager.web.SetBoardRoleForUserDto;
import com.nagygm.collaboard.boardmanager.web.UpdateBoardDetailsDto;
import com.nagygm.collaboard.whiteboard.web.RemoveAppUserFromBoardDto;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface BoardManagerService {
  @PreAuthorize("hasAuthority('ROLE_USER')")
  List<BoardDetailsWithPermissionDto> getAllBoards();
  
  @PreAuthorize("hasAuthority('ROLE_USER')")
  CreateBoardDto createBoard(CreateBoardDto dto);
  
  @PreAuthorize("hasAuthority('BOARD_READ_' + #urlHash)")
  BoardDetailsDto getBoardDetails(String urlHash);
  
  @PreAuthorize("hasAuthority('BOARD_OWNER_' + #urlHash)")
  boolean deleteBoardDetails(String urlHash);
  
  @PreAuthorize("hasAuthority('BOARD_OWNER_' + #dto.urlHash)")
  UpdateBoardDetailsDto updateBoardDetails(UpdateBoardDetailsDto dto);
  
  @PreAuthorize("hasAuthority('BOARD_ADMIN_' + #dto.boardUrl)")
  SetBoardRoleForUserDto setBoardRoleForAppUser(SetBoardRoleForUserDto dto);
  
  @PreAuthorize("hasAuthority('BOARD_ADMIN_' + #boardUrl)")
  List<BoardMemberDto> getAllBoardMembers(String boardUrl);
  
  @PreAuthorize("hasAuthority('BOARD_ADMIN_' + #dto.boardUrl)")
  RemoveAppUserFromBoardDto removeBoardUserAuthoritites(RemoveAppUserFromBoardDto dto);
  
  //TODO generating a user action, which is a link with id, and if accepted then confirms actions
  @PreAuthorize("hasAuthority('BOARD_OWNER_' + #urlHash)")
  Optional<Boolean> initateOwnershipChange();
}
