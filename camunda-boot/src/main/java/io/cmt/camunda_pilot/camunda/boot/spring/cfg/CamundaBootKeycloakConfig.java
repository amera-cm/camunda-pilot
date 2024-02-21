package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import javax.annotation.ParametersAreNonnullByDefault;
import lombok.Data;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@ParametersAreNonnullByDefault
public class CamundaBootKeycloakConfig {

  @Bean(destroyMethod = "close")
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public Keycloak keycloakAdmin(ClientRegistration kcReg, KcAdminClientCfg kcAdminClientCfg) {
    // TODO Create a Factory to control creation and connection state.
    return KeycloakBuilder.builder()
        .realm(kcAdminClientCfg.getRealm())
        .serverUrl(kcAdminClientCfg.getAuthServerUrl())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(kcReg.getClientId())
        .clientSecret(kcReg.getClientSecret())
        .build();
  }

  @Bean
  public ClientRegistration keycloakClientRegistration(ClientRegistrationRepository repo) {
    return repo.findByRegistrationId("keycloak");
  }

  @Bean
  @ConfigurationProperties(prefix = "keycloak.admin-client")
  public KcAdminClientCfg kcAdminClientCfg() {
    return new KcAdminClientCfg();
  }

  @Data
  public static class KcAdminClientCfg {
    private String realm;
    private String authServerUrl;
  }
}
