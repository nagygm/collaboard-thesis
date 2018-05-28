package com.nagygm.collaboard.auth.authorization.dal;

import com.nagygm.collaboard.auth.authorization.domain.UserBoardDetailsAuthority;
import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import com.nagygm.collaboard.user.domain.AppUser;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBoardDetailsAuthorityRepository extends CrudRepository<UserBoardDetailsAuthority, Long> {
  List<UserBoardDetailsAuthority> findByAppUser(AppUser appUser);
  List<UserBoardDetailsAuthority> findByBoardDetails(BoardDetails boardDetails);
  List<UserBoardDetailsAuthority> findByBoardDetailsAndAppUser(BoardDetails boardDetails, AppUser appUser);
  void deleteByAppUserAndBoardDetails(AppUser appUser, BoardDetails boardDetails);
}
