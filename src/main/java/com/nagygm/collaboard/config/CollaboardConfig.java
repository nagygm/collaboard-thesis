package com.nagygm.collaboard.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:collaboard.properties")
@Data
public class CollaboardConfig {
  boolean externalBrokerEnabled;
}
