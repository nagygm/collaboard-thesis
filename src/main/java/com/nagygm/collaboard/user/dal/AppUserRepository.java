package com.nagygm.collaboard.user.dal;


import com.nagygm.collaboard.user.domain.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
  Optional<AppUser> findByUsername(String username);
  List<AppUser> findAllByEmailOrUsername(String username, String email);
}
