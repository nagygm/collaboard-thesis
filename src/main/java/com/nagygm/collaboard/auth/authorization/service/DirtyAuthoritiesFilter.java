package com.nagygm.collaboard.auth.authorization.service;


import com.nagygm.collaboard.auth.authorization.domain.UpdatableUserDetails;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

public class DirtyAuthoritiesFilter extends GenericFilterBean implements
  ApplicationEventPublisherAware {
  
  private AuthenticationManager authenticationManager;
  private ApplicationEventPublisher eventPublisher;
  private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
  
  public DirtyAuthoritiesFilter(
    AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }
  
  @Override
  public void afterPropertiesSet() {
    Assert.notNull(authenticationManager, "authenticationManager must be specified");
  }
  
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !trustResolver.isAnonymous(authentication) && UpdatableUserDetails.class.isAssignableFrom(authentication
      .getPrincipal().getClass())) {
      UpdatableUserDetails userDetails = (UpdatableUserDetails) authentication.getPrincipal();
        if (userDetails.isDirty()) {
        HttpServletRequest req = (HttpServletRequest) request;

        PreAuthenticatedAuthenticationToken token =
          new PreAuthenticatedAuthenticationToken(
            userDetails,
            new Object(),
            userDetails.getAuthorities()
          );
        token.setDetails(new PreAuthenticatedGrantedAuthoritiesWebAuthenticationDetails(
          req, userDetails.getAuthorities()
        ));
        authenticationManager.authenticate(token);
        if (token.isAuthenticated()) {
          UpdatableUserDetails currentUserDetails = (UpdatableUserDetails) authentication.getPrincipal();
          currentUserDetails.setDirty(false);
          SecurityContextHolder.getContext().setAuthentication(token);
        }
      }
      
    }
    chain.doFilter(request, response);
  }
  
  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = eventPublisher;
  }
}
