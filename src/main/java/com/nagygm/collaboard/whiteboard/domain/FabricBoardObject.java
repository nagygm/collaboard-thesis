package com.nagygm.collaboard.whiteboard.domain;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class FabricBoardObject extends BoardObject {
  protected Integer angle;
  protected String backGroundColor;
  protected String clipTo;
  protected String fill;
  protected String fillRule;
  protected Boolean flipX;
  protected Boolean flipY;
  protected String globalCompositeOperation;
  protected Double height;
  protected Double left;
  protected Double opacity;
  protected String originX;
  protected String originY;
  protected String paintFirst;
  protected Double scaleX;
  protected Double scaleY;
  protected Double skewX;
  protected Double skewY;
  protected String stroke;
  protected String strokeLineCap;
  protected String strokeLineJoin;
  protected Integer strokeMiterLimit;
  protected Integer strokeWidth;
  protected Double top;
  protected String version;
  protected Boolean visible;
  protected Double width;
}
