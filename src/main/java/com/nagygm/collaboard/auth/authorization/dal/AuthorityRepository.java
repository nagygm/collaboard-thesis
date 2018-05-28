package com.nagygm.collaboard.auth.authorization.dal;

import com.nagygm.collaboard.auth.authorization.domain.Authority;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, String> {
  List<Authority> findAllByNameStartingWith(String condition);
}
