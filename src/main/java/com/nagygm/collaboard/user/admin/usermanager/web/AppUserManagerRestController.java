package com.nagygm.collaboard.user.admin.usermanager.web;

import com.nagygm.collaboard.user.admin.usermanager.service.AppUserManagerService;
import com.nagygm.collaboard.auth.authorization.domain.Role.Values.Const;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user-manager")
public class AppUserManagerRestController {
  final AppUserManagerService appUserManagerService;
  
  @Autowired
  public AppUserManagerRestController(AppUserManagerService appUserManagerService) {
    this.appUserManagerService = appUserManagerService;
  }
  
  /**
   * Return specific user data to the user manager page for admin pruposes.
   * @param username
   * @return
   */
  @GetMapping("/user/{username}")
  public AppUserDto getUser(@PathVariable("username") String username) {
    return appUserManagerService.getUser(username);
  }
  
  //TODO implement paging
  @GetMapping("/users")
  public List<AppUserDto> getUsers() {
    return appUserManagerService.getUsers();
  }
  
  @PutMapping("/user/{username}")
  public ResponseEntity activate(@PathVariable String username) {
    return simpleResponse(appUserManagerService.toggleActivation(username));
  }
  
  @DeleteMapping("/user/{username}")
  public ResponseEntity delete(@PathVariable("username") String username) {
    return simpleResponse(appUserManagerService.delete(username));
  }
  
  private ResponseEntity simpleResponse(Optional<Boolean> result) {
    return result.flatMap(it -> {
      if (it) {
        return Optional.of(ResponseEntity.ok().build());
      } else {
        return Optional.of(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
      }
    }).orElse(ResponseEntity.notFound().build());
  }
  
}
