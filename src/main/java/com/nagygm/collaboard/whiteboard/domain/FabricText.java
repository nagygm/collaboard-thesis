package com.nagygm.collaboard.whiteboard.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("fabric_i-text")
public class FabricText extends FabricBoardObject {
  protected int charSpacing;
  protected String fontFamily;
  protected int fontSize;
  protected String fontStyle;
  protected String fontWeight;
  protected double lineHeight;
  protected Boolean linethrough;
  protected Boolean overline;
  protected String shadow;
  protected Map<Integer, Map<Integer, Map<String, Object>>> styles;
  protected String text;
  protected String textAlign;
  protected String textBackgroundColor;
  protected String transformMatrix;
  protected boolean underline;
}
