package com.nagygm.collaboard.whiteboard.domain;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Domain class for representing the Board with the actual Data
 * The Id has to be the same as in details
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "boards")
public class BoardData {
  @Id
  private BigInteger id;
  @Indexed(unique = true)
  private String urlHash;
  private Collection<BoardObject> objects;
  private String version;
  private String drawingLibrary;
}
