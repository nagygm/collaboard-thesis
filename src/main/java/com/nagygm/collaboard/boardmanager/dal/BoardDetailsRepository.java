package com.nagygm.collaboard.boardmanager.dal;

import com.nagygm.collaboard.boardmanager.domain.BoardDetails;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDetailsRepository extends CrudRepository<BoardDetails, String> {
  @Cacheable(cacheNames = "boardDetailsCache", key="#p0")
  Optional<BoardDetails> findByUrlHash(String urlHash);
  @CacheEvict(cacheNames = "boardDetailsCache", key="#p0")
  int deleteByUrlHash(String urlHash);
  @CachePut(cacheNames = "boardDetailsCache", key="#p0.urlHash")
  BoardDetails save(BoardDetails boardDetails);
}
