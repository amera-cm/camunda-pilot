package io.cmt.camunda_pilot.web.api;

import io.cmt.camunda_pilot.spring.IamConfigData;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@ParametersAreNonnullByDefault
public class Api {

  private static Logger logger = LogManager.getLogger(Api.class);

  private final IamConfigData iamConfigData;
  private final ApplicationContext applicationContext;

  public Api(IamConfigData iamConfigData, ApplicationContext applicationContext) {
    this.iamConfigData = iamConfigData;
    this.applicationContext = applicationContext;
  }

  @GetMapping("/api/protected")
  public String index(@AuthenticationPrincipal Jwt jwt, Authentication auth) {
    logger.info(auth.getPrincipal());
    auth.getAuthorities().forEach(logger::info);
    //    jwt.getClaimAsMap("realm_access").get("roles")
    return String.format("Hello, %s!", jwt.getClaimAsString("preferred_username"));
  }

  @GetMapping("/api/premium")
  public String premium(@AuthenticationPrincipal Jwt jwt, Authentication auth) {
    logger.info(auth.getPrincipal());
    auth.getAuthorities().forEach(logger::info);
    return String.format("Hello, %s!", jwt.getClaimAsString("preferred_username"));
  }

  @GetMapping("/api/admin")
  public Map<String, Object> admin(@AuthenticationPrincipal Jwt jwt) {
    final var kc = applicationContext.getBean(Keycloak.class);
    final var realm = kc.realm(iamConfigData.getRealm());
    final var user = realm.users().get(jwt.getSubject());
    final var roles = user.roles().getAll();
    return Map.of("user", user.toRepresentation(), "roles", roles);
  }
}
