package com.nagygm.collaboard.whiteboard.dal;

import com.nagygm.collaboard.whiteboard.domain.BoardData;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDataRepository extends MongoRepository<BoardData, BigInteger> {
  @Override
  Optional<BoardData> findById(BigInteger id);
  int deleteByUrlHash(String urlHash);
}
