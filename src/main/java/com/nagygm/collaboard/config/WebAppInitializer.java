package com.nagygm.collaboard.config;

import org.springframework.core.annotation.Order;


/**
 * web.xml alternative for Java programmed configuration, a Serlvet 3.0 or above will pick this up
 * automaticaly
 */
@Order(1) // Filters declared at the Dispatcher initializer should be registered first
public class WebAppInitializer {
//  extends AbstractAnnotationConfigDispatcherServletInitializer {
  
//  public static final String CHAR_ENCODING = "UTF-8";
//
//  @Override
//  protected Class<?>[] getRootConfigClasses() {
//    return new Class<?>[]{AppConfig.class, MongoDbConfig.class, RelationalDbConfig.class};
//  }
//
//  /**
//   * Add servlet related configuration classes
//   */
//  @Override
//  protected Class<?>[] getServletConfigClasses() {
//    return new Class<?>[]{WebConfig.class, WebSocketConfig.class, WebSecurityConfig.class,
//      WebSocketSecurityConfig.class};
//  }
//
//  /**
//   * Default servlet mapping for dispatcher
//   * @return
//   */
//  @Override
//  protected String[] getServletMappings() {
//    return new String[]{"/"};
//  }
//
//  /**
//   * To allow Optinons request against endpoints
//   * @param registration
//   */
//  @Override
//  protected void customizeRegistration(Dynamic registration) {
//    registration.setInitParameter("dispatchOptionsRequest", "true");
//  }
//
//  @Override
//  protected Filter[] getServletFilters() {
//    final CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
//    encodingFilter.setEncoding(CHAR_ENCODING);
//    encodingFilter.setForceEncoding(true);
//    return new Filter[]{encodingFilter};
//  }
}
