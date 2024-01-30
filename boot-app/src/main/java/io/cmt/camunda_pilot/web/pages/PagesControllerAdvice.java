package io.cmt.camunda_pilot.web.pages;

import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@ParametersAreNonnullByDefault
public class PagesControllerAdvice {
  @ModelAttribute
  public void addAttributes(Model model) {
    model.addAttribute("loginPath", "/oauth2/authorization/keycloak");
  }
}
