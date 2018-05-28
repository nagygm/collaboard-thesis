package com.nagygm.collaboard.common.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Configurable
public class ErrorsSerializer extends JsonSerializer<Errors> {
  @Autowired
  private MessageSource messageSource;
  
  @Override
  public void serialize(Errors value, JsonGenerator gen, SerializerProvider serializers)
    throws IOException {
    Locale locale = LocaleContextHolder.getLocale();
    gen.writeStartObject();
    if(value.hasErrors()) {
      gen.writeObjectFieldStart("fieldErrors");
      for (FieldError it : value.getFieldErrors()) {
        gen.writeStringField(it.getField(), messageSource.getMessage(it, locale));
      }
      gen.writeEndObject();
      gen.writeArrayFieldStart("globalErrors");
      for (ObjectError it : value.getGlobalErrors()) {
        gen.writeString(messageSource.getMessage(it, locale));
      }
      gen.writeEndArray();
    }
    gen.writeEndObject();
  }
}
