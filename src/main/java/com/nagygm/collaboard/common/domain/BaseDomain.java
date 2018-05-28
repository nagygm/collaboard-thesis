package com.nagygm.collaboard.common.domain;

import lombok.Data;
import org.springframework.validation.Errors;

@Data
public class BaseDomain {
  Errors errors;
}
