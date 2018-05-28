package com.nagygm.collaboard.auth.authorization.service;

import com.nagygm.collaboard.auth.authorization.domain.BoardRoleValues;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.user.domain.AppUser;
import org.springframework.stereotype.Service;

@Service
public interface BoardAuthoritiesService {
  void addToUserForBoardDetails(AppUser appUser, BoardDetails boardDetails, BoardRoleValues role);
  void delete(AppUser appUser, BoardDetails boardDetails);
  boolean isOwner(AppUser appUser, BoardDetails boardDetails);
}
