package io.cmt.camunda_pilot.camunda.boot.spring.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KcLogoutHandler implements LogoutHandler {

  private static final Logger logger = LogManager.getLogger(KcLogoutHandler.class);
  private final RestTemplate restTemplate;

  public KcLogoutHandler() {
    this.restTemplate = new RestTemplate();
  }

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication auth) {
    SecurityContextHolder.getContext().setAuthentication(null);
    final var session = request.getSession(false);
    invalidateSession(session);
    logoutFromKeycloak((OidcUser) auth.getPrincipal());
  }

  private void logoutFromKeycloak(OidcUser user) {
    String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(endSessionEndpoint)
            .queryParam("id_token_hint", user.getIdToken().getTokenValue());

    ResponseEntity<String> logoutResponse =
        restTemplate.getForEntity(builder.toUriString(), String.class);
    if (logoutResponse.getStatusCode().is2xxSuccessful()) {
      logger.info("Successfully logged out from Keycloak");
    } else {
      logger.error("Could not propagate logout to Keycloak");
    }
  }

  private void invalidateSession(HttpSession session) {
    try {
      if (session != null) {
        session.invalidate();
      }
    } catch (Exception e) {
      logger.error("Error trying to invalidate session", e);
    }
  }
}
