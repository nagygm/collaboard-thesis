package com.nagygm.collaboard.auth.authorization.dal;

import com.nagygm.collaboard.auth.authorization.domain.BoardAuthority;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardAuthorityRepository extends CrudRepository<BoardAuthority, Integer> {
    List<BoardAuthority> findAll();
}
