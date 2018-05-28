package com.nagygm.collaboard.user.profile.web;

import com.nagygm.collaboard.auth.authentication.SecurityService;
import com.nagygm.collaboard.user.service.AppUserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUserController {
  final AppUserService appUserService;
  final SecurityService securityService;
  
  @Autowired
  public AppUserController(AppUserService appUserService, SecurityService securityService) {
    this.appUserService = appUserService;
    this.securityService = securityService;
  }
  
  @GetMapping("/login")
  public String login() {
    return "user/login";
  }
  
  @RequestMapping("/login-error")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "user/login";
  }
  
  @GetMapping(value = "/logout")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return "redirect:/";
  }
  
  @GetMapping(value = "/registration")
  public ModelAndView registration() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("registrationDto", new RegistrationDto());
    modelAndView.setViewName("user/registration");
    return modelAndView;
  }
  
  @PostMapping(value = "/registration")
  public String registration(@Valid RegistrationDto dto, BindingResult bindingResult, Model model) {
    dto.setErrors(bindingResult);
    dto = appUserService.registerUser(dto);
    if (dto.getErrors().hasErrors()) {
      model.addAttribute("registrationDto", dto);
      return "user/registration";
    } else {
//      securityService.autologin(dto.getUsername(), dto.getPassword());
      return "redirect:/";
    }
  }
}
