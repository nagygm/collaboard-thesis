package com.nagygm.collaboard.whiteboard.service;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.bson.types.ObjectId;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
  
  @Override
  public ObjectId deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException, JsonProcessingException {
    JsonNode jsonNode = p.getCodec().readTree(p);
    return new ObjectId(jsonNode.asText());
  }
}
