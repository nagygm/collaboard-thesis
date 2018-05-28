package com.nagygm.collaboard.config;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

import com.nagygm.collaboard.auth.authorization.service.WebSocketSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig
  extends AbstractSecurityWebSocketMessageBrokerConfigurer {
  final WebSocketSecurityService webSocketSecurityService;
  
  @Autowired
  public WebSocketSecurityConfig(WebSocketSecurityService webSocketSecurityService) {
    this.webSocketSecurityService = webSocketSecurityService;
  }
  
  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages
      .nullDestMatcher().authenticated()
      .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
      .simpSubscribeDestMatchers("/topic/board/*")
      .access("@webSocketSecurityService.checkAuthorization(message,'BOARD_READ_')")
      .simpDestMatchers("/collaboard/*")
      .access("@webSocketSecurityService.checkAuthorization(message,'BOARD_WRITE_')")
      .anyMessage().denyAll();
  }
}
