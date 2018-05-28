package com.nagygm.collaboard.whiteboard.dal;

import static org.springframework.data.mongodb.core.query.Query.query;

import com.mongodb.client.result.UpdateResult;
import com.nagygm.collaboard.whiteboard.domain.BoardData;
import com.nagygm.collaboard.whiteboard.domain.BoardObject;
import com.nagygm.collaboard.whiteboard.domain.FabricBoardObject;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class FabricBoardObjectDao {
  final MongoOperations mongoOperations;
  
  private static Logger log = LogManager.getLogger(FabricBoardObjectDao.class);
  
  @Autowired
  public FabricBoardObjectDao(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }
  
  public UpdateResult saveBoardObject(BigInteger boardId, FabricBoardObject fabricBoardObject) {
    Query query = new Query(Criteria.where("_id").is(boardId));
    Update update = new Update().push("objects", fabricBoardObject);
    return mongoOperations.updateFirst(query, update, BoardData.class, "boards");
  }
  
  public void updateBoardObject(BigInteger boardId, FabricBoardObject fabricBoardObject) {
    Query query = new Query(Criteria.where("_id").is(boardId).and("objects._id").is(new ObjectId(fabricBoardObject.getId())));
    Update update = new Update().set("objects.$", fabricBoardObject);
    log.info(mongoOperations.findAndModify(query, update, fabricBoardObject.getClass(), "boards"));
  }
  
  public void deleteBoardObject(BigInteger boardId, String objectId) {
    Query query = new Query(Criteria.where("_id").is(boardId));
    Update update = new Update().pull("objects", Query.query(Criteria.where("_id").is(objectId)));
    log.info(mongoOperations.updateFirst(query, update, BoardData.class, "boards"));
  }
  
  public Optional<BoardObject> getBoardObejct(BigInteger boardId, String objectId) {
    Query query = new Query(Criteria.where("_id").is(boardId).and("objects")
      .elemMatch(Criteria.where("_id").is(objectId)));
    query.limit(1);
    List<BoardData> objects = mongoOperations.find(query, BoardData.class, "boards");
    return objects.get(0).getObjects().stream().findFirst();
  }
}
