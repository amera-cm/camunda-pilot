package io.cmt.camunda_pilot.boot.web.pages;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LogoutPage {

  private final LogoutHandler logoutHandler;

  public LogoutPage(LogoutHandler logoutHandler) {
    this.logoutHandler = logoutHandler;
  }

  @GetMapping("/logout")
  public RedirectView logout(
      Authentication auth, HttpServletRequest request, HttpServletResponse response) {
    logoutHandler.logout(request, response, auth);
    return new RedirectView("/ui");
  }
}
