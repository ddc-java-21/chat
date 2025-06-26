package edu.cnm.deepdive.chat.controller;

import edu.cnm.deepdive.chat.model.entity.User;
import edu.cnm.deepdive.chat.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final AbstractUserService service;

  @Autowired
  UserController(AbstractUserService service) {
    this.service = service;
  }

  @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public User get() {
    return service.getMe(service.getCurrentUser());
  }

  // TODO: 6/26/25 Implement additional controller methods for users.
}
