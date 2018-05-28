package com.nagygm.collaboard.config;

import com.nagygm.collaboard.auth.authorization.service.DirtyAuthoritiesFilter;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
  private final UserDetailsService userDetailsService;
  private final AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthUserDetailsService;
  
  @Autowired
  public WebSecurityConfig(UserDetailsService userDetailsService,
    AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> preAuthUserDetailsService) {
    super();
    this.userDetailsService = userDetailsService;
    this.preAuthUserDetailsService = preAuthUserDetailsService;
  }
  
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }
  
  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
      .addFilterAfter(new DirtyAuthoritiesFilter(authenticationManager()),
        SessionManagementFilter.class)
      .authorizeRequests()
      .antMatchers("/", "/index", "/resources/**").permitAll()
      .antMatchers("/registration").anonymous()
      .antMatchers("/admin/**").hasRole("ADMIN")
      .antMatchers("/user/**").hasRole("USER")
      .antMatchers("/board/**").hasRole("USER")
      .antMatchers("/board-manager/**", "/board-manager").hasRole("USER")
      .and()
      .formLogin().loginPage("/login").permitAll().failureUrl("/login-error")
      .and()
      .logout().invalidateHttpSession(true).permitAll()
      .and().exceptionHandling()
      .accessDeniedPage("/403")
      .and()
      .sessionManagement()
      .sessionAuthenticationStrategy(compositeSessionAuthenticationStrategy()).maximumSessions(1)
      .sessionRegistry(sessionRegistry());
  }
  
  @Override
  public void configure(WebSecurity web) {
    web
      .ignoring()
      .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
  }
  
  @Autowired
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
    auth.authenticationProvider(preAuthenticatedAuthenticationProvider(preAuthUserDetailsService))
    ;
  }
  
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
  
  
  @Bean("sessionRegistry")
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
  
  @Bean
  public CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy() {
    ArrayList<SessionAuthenticationStrategy> list = new ArrayList<>();
    list.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));
    //TODO incorporate session fixation and SessionId change strategy, with proper session reauth
    return new CompositeSessionAuthenticationStrategy(list);
  }
  
  /**
   * To remove any expired and timedout session-s from the session registry
   * @return
   */
  @Bean
  public static ServletListenerRegistrationBean httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
  }
  
  
  public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(
    AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsService) {
    PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
    provider.setPreAuthenticatedUserDetailsService(userDetailsService);
    return provider;
  }
}
