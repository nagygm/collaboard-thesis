package com.nagygm.collaboard.auth.authorization.service;

import com.nagygm.collaboard.auth.authentication.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSecurityService {

  private final SecurityService securityService;
  
  @Autowired
  public WebSocketSecurityService(
    SecurityService securityService) {
    this.securityService = securityService;
  }
  
  public boolean checkAuthorization(Message<?> message, String boardAuthority) {
    StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
    String topic = stompHeaderAccessor.getDestination();
    String[] locationParts = topic.split("/");
    String urlHash = locationParts[locationParts.length-1];
    return securityService.hasAuthority(boardAuthority + urlHash);
  }
}
