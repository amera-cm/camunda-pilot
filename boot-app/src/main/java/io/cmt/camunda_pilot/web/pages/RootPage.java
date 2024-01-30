package io.cmt.camunda_pilot.web.pages;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@ParametersAreNonnullByDefault
public class RootPage {

  @GetMapping
  public String root(
      @AuthenticationPrincipal @Nullable OidcUser principal,
      Authentication authentication,
      Model model) {
    if (principal != null) {
      model.addAttribute("authenticated", true);
      model.addAttribute("name", principal.getIdToken().getFullName());
      model.addAttribute("username", principal.getIdToken().getPreferredUsername());
      model.addAttribute("email", principal.getIdToken().getEmail());
      model.addAttribute("authorities", authentication.getAuthorities());
    }
    return "root";
  }
}
