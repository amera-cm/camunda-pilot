package io.cmt.camunda_pilot.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

/** Spring Security Config. */
@Configuration
@EnableWebSecurity
@ParametersAreNonnullByDefault
public class SecurityConfig {

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String GROUPS_CLAIM = "groups";
  private static final String SCOPE_CLAIM = "scope";

  @Bean
  @ConfigurationProperties(prefix = "iam")
  public IamConfigData iamConfigData() {
    return new IamConfigData();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, IamConfigData configData, ClientRegistration keycloak) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .oauth2ResourceServer(
            cfg -> cfg.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter())))
        .addFilterAfter(
            createPolicyEnforcerFilter(configData, keycloak),
            BearerTokenAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public ClientRegistration keycloakClientRegistration(ClientRegistrationRepository repo) {
    return repo.findByRegistrationId("keycloak");
  }

  @Bean(destroyMethod = "close")
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public Keycloak keycloakAdmin(IamConfigData configData, ClientRegistration kcReg) {
    // TODO Create a Factory to control creation and connection state.
    return KeycloakBuilder.builder()
        .realm(configData.getRealm())
        .serverUrl(configData.getAuthServerUrl())
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .clientId(kcReg.getClientId())
        .clientSecret(kcReg.getClientSecret())
        .build();
  }

  @Nonnull
  private ServletPolicyEnforcerFilter createPolicyEnforcerFilter(
      IamConfigData configData, ClientRegistration keycloak) {
    final var config = new PolicyEnforcerConfig();
    config.setRealm(configData.getRealm());
    config.setAuthServerUrl(configData.getAuthServerUrl());
    config.setResource(keycloak.getClientId());
    config.setCredentials(Map.of("secret", keycloak.getClientSecret()));
    return new ServletPolicyEnforcerFilter(request -> config);
  }

  @ParametersAreNonnullByDefault
  private static class JwtAuthenticationConverter
      implements Converter<Jwt, AbstractAuthenticationToken> {
    @SuppressWarnings("unchecked")
    @Nonnull
    private static List<SimpleGrantedAuthority> jwtToGrantedAuthorities(Jwt jwt) {
      final var authorities = new ArrayList<SimpleGrantedAuthority>();
      if (jwt.hasClaim(REALM_ACCESS_CLAIM)) {
        final var realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        final var roles = (List<String>) realmAccess.get(ROLES_CLAIM);
        authorities.addAll(fromAuthNames(roles, "ROLE_"));
      }
      if (jwt.hasClaim(GROUPS_CLAIM)) {
        final var groups = jwt.getClaimAsStringList(GROUPS_CLAIM);
        authorities.addAll(fromAuthNames(groups, "GROUP_"));
      }
      if (jwt.hasClaim(SCOPE_CLAIM)) {
        final var scopes = Arrays.asList(jwt.getClaimAsString(SCOPE_CLAIM).split(" "));
        authorities.addAll(fromAuthNames(scopes, "SCOPE_"));
      }
      return authorities;
    }

    @Nonnull
    private static List<SimpleGrantedAuthority> fromAuthNames(
        List<String> authNames, @Nullable String prefix) {
      final var _prefix = Objects.requireNonNullElse(prefix, "");
      return authNames.stream()
          .map(name -> new SimpleGrantedAuthority(_prefix + name.toUpperCase(Locale.ROOT)))
          .toList();
    }

    @Nonnull
    public AbstractAuthenticationToken convert(Jwt jwt) {
      return new JwtAuthenticationToken(jwt, jwtToGrantedAuthorities(jwt));
    }
  }
}
