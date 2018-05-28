package com.nagygm.collaboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@EnableCaching
@ComponentScan(excludeFilters = {
  @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)},
  basePackages = {"com.nagygm.collaboard.config"}
)
public class Application {
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
