package com.nagygm.collaboard.user.admin.usermanager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppUserManagerController {
  @RequestMapping("/admin/user-manager")
  public String userManager() {
    return "admin/user-manager";
  }
}
