package io.cmt.camunda_pilot.camunda.boot.spring.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@ParametersAreNonnullByDefault
public class KcCamundaAuthenticationProvider extends ContainerBasedAuthenticationProvider {

  private static final Logger logger = LogManager.getLogger(KcCamundaAuthenticationProvider.class);

  @SuppressWarnings("unchecked")
  @Override
  public AuthenticationResult extractAuthenticatedUser(
      HttpServletRequest request, ProcessEngine engine) {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.isAuthenticated()) {
      if (authentication instanceof OAuth2AuthenticationToken authToken) {
        final var oidcUser = (OidcUser) authToken.getPrincipal();
        final var groups = (List<String>) oidcUser.getAttributes().get("groups");
        final var result = new AuthenticationResult(oidcUser.getName(), true);
        result.setGroups(groups);
        return result;
      } else {
        logger.error("Authentication must be an instance of OAuth2AuthenticationToken");
      }
    }
    return AuthenticationResult.unsuccessful();
  }
}
