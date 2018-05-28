package com.nagygm.collaboard.common.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

@Data
public abstract class BaseDto {
  @JsonSerialize(using = ErrorsSerializer.class)
  private Errors errors = new DirectFieldBindingResult(this, "dto");
}
