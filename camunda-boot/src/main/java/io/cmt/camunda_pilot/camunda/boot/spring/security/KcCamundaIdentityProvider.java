package io.cmt.camunda_pilot.camunda.boot.spring.security;

import org.camunda.bpm.extension.keycloak.plugin.KeycloakIdentityProviderPlugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "camunda-plugins.keycloak")
public class KcCamundaIdentityProvider extends KeycloakIdentityProviderPlugin {}
