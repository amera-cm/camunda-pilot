package io.cmt.camunda_pilot.camunda.boot.web.pages;

import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ParametersAreNonnullByDefault
public class HomePage {

  @GetMapping("/")
  public String root(Authentication auth) {
    return "ROOT!!";
  }

  @GetMapping("/home")
  public String home(Authentication auth) {
    return "HOME!!";
  }
}
