package com.nagygm.collaboard.auth.authorization.service;

import com.nagygm.collaboard.auth.authorization.dal.BoardAuthorityRepository;
import com.nagygm.collaboard.auth.authorization.dal.UserBoardDetailsAuthorityRepository;
import com.nagygm.collaboard.auth.authorization.domain.BoardAuthority;
import com.nagygm.collaboard.auth.authorization.domain.BoardRoleValues;
import com.nagygm.collaboard.auth.authorization.domain.UserBoardDetailsAuthority;
import com.nagygm.collaboard.auth.authorization.domain.UserBoardDetailsAuthorityId;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.user.domain.AppUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardAuthoritiesServiceImpl implements BoardAuthoritiesService{
  private final BoardAuthorityRepository boardAuthorityRepository;
  private final UserBoardDetailsAuthorityRepository userBoardDetailsAuthorityRepository;
  
  @Autowired
  public BoardAuthoritiesServiceImpl(BoardAuthorityRepository boardAuthorityRepository,
    UserBoardDetailsAuthorityRepository userBoardDetailsAuthorityRepository) {
    this.boardAuthorityRepository = boardAuthorityRepository;
    this.userBoardDetailsAuthorityRepository = userBoardDetailsAuthorityRepository;
  }
  
  @Transactional
  @Override
  public void addToUserForBoardDetails(AppUser appUser, BoardDetails boardDetails,
    BoardRoleValues role) {
    Iterable<BoardAuthority> boardAuthorities = resolveAuthortiesForRole(role);
    //TODO remove this hack using BaardRole in database
    userBoardDetailsAuthorityRepository.deleteByAppUserAndBoardDetails(appUser, boardDetails);
    boardAuthorities.forEach(
      it -> {
        UserBoardDetailsAuthority authority = new UserBoardDetailsAuthority();
        authority.setBoardAuthority(it);
        authority.setAppUser(appUser);
        authority.setBoardDetails(boardDetails);
        authority.setId(new UserBoardDetailsAuthorityId(appUser.getId(),boardDetails.getId(),it.getId()));
        userBoardDetailsAuthorityRepository.save(authority);
      });
    
  }
  
  @Override
  public void delete(AppUser appUser, BoardDetails boardDetails) {
    userBoardDetailsAuthorityRepository.deleteByAppUserAndBoardDetails(appUser, boardDetails);
  }
  
  @Override
  public boolean isOwner(AppUser appUser, BoardDetails boardDetails) {
    List<UserBoardDetailsAuthority> userBoardDetailsAuthority =
      userBoardDetailsAuthorityRepository.findByBoardDetailsAndAppUser(boardDetails, appUser);
    return userBoardDetailsAuthority.stream()
      .anyMatch(it -> it.getBoardAuthority().getName().equals(BoardRoleValues.BOARD_OWNER.name()));
  }
  
  private Iterable<BoardAuthority> resolveAuthortiesForRole(BoardRoleValues role) {
    return boardAuthorityRepository.findAllById(role.getAllforRole());
  }
}
