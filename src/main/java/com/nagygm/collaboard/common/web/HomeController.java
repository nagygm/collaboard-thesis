package com.nagygm.collaboard.common.web;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webjars.RequireJS;

@Controller
public class HomeController {
  /**
   * Configurer for requirejs finding webjars automatically
   * @return returns javascript configuration for client
   */
  @ResponseBody
  @GetMapping(value = "/webjars.js", produces = "application/javascript")
  public String webjarjs() {
    return RequireJS.getSetupJavaScript("/webjars/");
  }
  
  @GetMapping(value = {"/", "/index"})
  public String landingPage(HttpSession session) {
    session.getId();
    return "index";
  }
  
}
