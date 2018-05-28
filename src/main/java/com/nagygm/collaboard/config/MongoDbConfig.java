package com.nagygm.collaboard.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.nagygm.collaboard.whiteboard")
public class MongoDbConfig extends AbstractMongoConfiguration {
  
  @Autowired
  private Environment env;
  
//  @Bean
//  public MongoOperations mongoTemplate(Mongo mongo) {
//    return new MongoTemplate(mongoClient(), "collaboard");
//  }
  
  @Override
  protected String getDatabaseName() {
    return env.getProperty("spring.data.mongodb.database");
  }
  
  @Override
  public MongoClient mongoClient() {
    MongoCredential credential = MongoCredential.createCredential(
      env.getProperty("spring.data.mongodb.username"),
      env.getProperty("spring.data.mongodb.authenticationDatabase"),
      env.getProperty("spring.data.mongodb.password").toCharArray());
    return new MongoClient(
      new ServerAddress(
        env.getProperty("spring.data.mongodb.host"),
        Integer.parseInt(env.getProperty("spring.data.mongodb.port"))
      ),
      credential,
      MongoClientOptions.builder().build()
    );
  }
  
  @Bean
  public CustomConversions customConversions() {
    return new MongoCustomConversions(Collections.emptyList());
  }
}
