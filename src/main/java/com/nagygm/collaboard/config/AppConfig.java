package com.nagygm.collaboard.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = {"com.nagygm.collaboard"},
  excludeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class),
    @Filter(type = FilterType.ANNOTATION, value = Controller.class),
    @Filter(type = FilterType.ANNOTATION, value = Configuration.class)
  })
public class AppConfig {
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
