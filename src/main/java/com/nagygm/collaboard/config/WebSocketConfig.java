package com.nagygm.collaboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  
  @Autowired
  CollaboardConfig collaboardConfig;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/whiteboard").withSockJS();
  }
  
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    if (collaboardConfig.externalBrokerEnabled) {
      registry.enableStompBrokerRelay("/queue/", "/topic/");
    } else {
      registry.enableSimpleBroker("/queue/", "/topic/");
    }
    registry.setApplicationDestinationPrefixes("/collaboard");
  }
  
}
