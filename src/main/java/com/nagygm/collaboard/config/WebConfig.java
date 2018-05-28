package com.nagygm.collaboard.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nagygm.collaboard",
  excludeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class),
    @Filter(type = FilterType.ANNOTATION, value = Service.class),
    @Filter(type = FilterType.ANNOTATION, value = Repository.class),
    @Filter(type = FilterType.ANNOTATION, value = Configuration.class)
  })
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {
  
  private ApplicationContext applicationContext;
  private final String CHAR_ENCODING = "UTF-8";
  
  
  @Bean
  public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
    throws IOException {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    MutablePropertySources propertySources = new MutablePropertySources();
    Resource resource = new DefaultResourceLoader().getResource("classpath:application.yml");
    YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
    //TODO this is just a hack
    List<PropertySource<?>> yamlProperties = sourceLoader.load("yamlProperties", resource);
    yamlProperties.forEach(propertySources::addFirst);
    configurer.setPropertySources(propertySources);
    return configurer;
  }
  
  @Autowired
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
  
  /**
   * Adding resource handler
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //Here the pattern /static/** is the URL path pattern
    registry.addResourceHandler("/static/**")
      .addResourceLocations("/resources/", "/WEB-INF/view/", "classpath:/static/",
        "classpath:/templates/")
      .resourceChain(true)
      .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
  }
  
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    sessionLocaleResolver.setDefaultLocale(Locale.forLanguageTag("hu"));
    return sessionLocaleResolver;
  }
  
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    return localeChangeInterceptor;
  }
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
  
  @Profile("dev")
  @Bean(name = "messageSource")
  public ReloadableResourceBundleMessageSource devMessageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:i18n/messages");
    messageSource.setCacheMillis(0);
    messageSource.setDefaultEncoding(CHAR_ENCODING);
    return messageSource;
  }
  
  @Profile("prod")
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("i18n/messages");
    messageSource.setDefaultEncoding(CHAR_ENCODING);
    return messageSource;
  }
  
  @Bean
  public FormattingConversionService conversionService() {
    DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(
      false);
    conversionService
      .addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
    DateFormatterRegistrar registrar = new DateFormatterRegistrar();
    registrar.setFormatter(new DateFormatter("yyyyMMdd"));
    registrar.registerFormatters(conversionService);
    
    return conversionService;
  }
  
  //Thymeleaf configuration
  @Bean
  public ViewResolver htmlViewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine(htmlTemplateResolver()));
    resolver.setCharacterEncoding(CHAR_ENCODING);
    resolver.setViewNames(new String[]{"*.html"});
    return resolver;
  }
  
  private ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addDialect(new Java8TimeDialect());
    templateEngine.setTemplateResolver(templateResolver);
    templateEngine.setEnableSpringELCompiler(true);
    return templateEngine;
  }
  
  private ITemplateResolver htmlTemplateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setApplicationContext(applicationContext);
    resolver.setPrefix("classpath:/templates/");
    resolver.setCacheable(false);
    resolver.setTemplateMode(TemplateMode.HTML);
    return resolver;
  }
  
  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.viewResolver(htmlViewResolver());
  }
}
