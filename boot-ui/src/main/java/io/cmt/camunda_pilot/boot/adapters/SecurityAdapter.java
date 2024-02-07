package io.cmt.camunda_pilot.boot.adapters;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class SecurityAdapter {

  private final AuthenticationContext authenticationContext;

  public SecurityAdapter(AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
  }

  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public OidcUser getUser() {
    return authenticationContext.getAuthenticatedUser(OidcUser.class).get();
  }

  public void logout(UI ui) {
    ui.getSession().close();
    ui.getPage().setLocation("/logout");
  }
}
