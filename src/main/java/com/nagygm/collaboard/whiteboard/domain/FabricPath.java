package com.nagygm.collaboard.whiteboard.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("fabric_path")
public class FabricPath extends FabricBoardObject {
  
  private List<PathCommand> path;
  
  @Data
  @JsonSerialize(using = PathCommandSerializer.class)
  @JsonDeserialize(using = PathCommandDeserializer.class)
  public static class PathCommand {
    
    private String op;
    private List<Double> coords;
  }
  
  public static class PathCommandDeserializer extends StdDeserializer<PathCommand> {
    
    protected PathCommandDeserializer() {
      super(PathCommand.class);
    }
    
    protected PathCommandDeserializer(Class<PathCommand> clazz) {
      super(clazz);
    }
    
    @Override
    public PathCommand deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
      ArrayNode arrayNode = p.getCodec().readTree(p);
      
      PathCommand pathCommand = new PathCommand();
      pathCommand.setCoords(new ArrayList<>());
      
      pathCommand.setOp(arrayNode.get(0).asText());
      for (int i = 1; i < arrayNode.size(); i++) {
        pathCommand.getCoords().add(arrayNode.get(i).asDouble());
      }
      return pathCommand;
    }
  }
  
  public static class PathCommandSerializer extends StdSerializer<PathCommand> {
    
    protected PathCommandSerializer() {
      super(PathCommand.class);
    }
    
    protected PathCommandSerializer(Class<PathCommand> clazz) {
      super(clazz);
    }
    
    @Override
    public void serialize(PathCommand value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
      gen.writeStartArray();
      gen.writeString(value.getOp());
      for (Double coord : value.getCoords()) {
        gen.writeNumber(coord);
      }
      gen.writeEndArray();
    }
  }
}
