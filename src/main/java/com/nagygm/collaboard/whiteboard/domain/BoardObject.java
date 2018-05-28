package com.nagygm.collaboard.whiteboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nagygm.collaboard.whiteboard.service.ObjectIdDeserializer;
import com.nagygm.collaboard.whiteboard.service.ObjectIdSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({
  @Type( value=FabricPath.class, name = "fabric_path"),
  @Type( value=FabricText.class, name = "fabric_i-text"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BoardObject {
  //TODO move to base class
//  @JsonSerialize(using = ObjectIdSerializer.class)
//  @JsonDeserialize(using = ObjectIdDeserializer.class)
  @org.springframework.data.annotation.Id
  @Indexed
  protected String id;
  @Transient
  protected String tempId;
}
