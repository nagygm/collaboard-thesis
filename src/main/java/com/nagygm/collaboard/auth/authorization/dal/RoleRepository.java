package com.nagygm.collaboard.auth.authorization.dal;

import com.nagygm.collaboard.auth.authorization.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
  Role findByName(String name);
}
