package io.cmt.camunda_pilot.camunda.boot.ops;

import io.cmt.camunda_pilot.camunda.boot.spring.cfg.CamundaBootKeycloakConfig.KcAdminClientCfg;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class UsersOps {

  private final Keycloak keycloak;
  private final KcAdminClientCfg clientCfg;

  public UsersOps(Keycloak keycloak, KcAdminClientCfg clientCfg) {
    this.keycloak = keycloak;
    this.clientCfg = clientCfg;
  }

  @Nonnull
  public RealmResource realm() {
    return keycloak.realm(clientCfg.getRealm());
  }

  @ParametersAreNonnullByDefault
  public List<UserRepresentation> roleUserMembers(String role) {
    final var roleRes = realm().roles().get(role);
    return roleRes.getUserMembers();
  }
}
