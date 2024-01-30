package io.cmt.camunda_pilot.spring;

import io.cmt.camunda_pilot.spring.security.KeycloakLogoutHandler;
import io.cmt.camunda_pilot.spring.security.PolicyEnforcerFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/** Spring Security Config. */
@Configuration
@ComponentScan(basePackageClasses = {KeycloakLogoutHandler.class})
@EnableWebSecurity
@ParametersAreNonnullByDefault
public class SecurityConfig {

  private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String SCOPE_CLAIM = "scope";

  @Nonnull private final LogoutHandler logoutHandler;

  public SecurityConfig(LogoutHandler logoutHandler) {
    this.logoutHandler = logoutHandler;
  }

  @Bean
  @ConfigurationProperties(prefix = "iam")
  public IamConfigData iamConfigData() {
    return new IamConfigData();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, IamConfigData configData, ClientRegistration kgReg, Keycloak kcAdmin)
      throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/", "/public/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            cfg -> cfg.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter())))
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.addLogoutHandler(logoutHandler).logoutSuccessUrl("/"));
    //        .addFilterAfter(
    //            createPolicyEnforcerFilter(configData, kgReg),
    // BearerTokenAuthenticationFilter.class);
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

  @SuppressWarnings("unchecked")
  @Bean
  public GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return authorities -> {
      final var mappedAuthorities = new HashSet<GrantedAuthority>();
      for (GrantedAuthority authority : authorities) {
        if (authority instanceof OidcUserAuthority oidcAuth) {
          final var roles =
              ((ArrayList<String>)
                      oidcAuth.getUserInfo().getClaimAsMap("realm_access").get("roles"))
                  .stream()
                      .map(r -> new SimpleGrantedAuthority(r.toUpperCase(Locale.ROOT)))
                      .toList();
          mappedAuthorities.addAll(roles);
        }
        mappedAuthorities.add(authority);
      }
      return mappedAuthorities;
    };
  }

  @Nonnull
  private PolicyEnforcerFilter createPolicyEnforcerFilter(
      IamConfigData configData, ClientRegistration kcReg) {
    final var config = new PolicyEnforcerConfig();
    config.setRealm(configData.getRealm());
    config.setAuthServerUrl(configData.getAuthServerUrl());
    config.setResource(kcReg.getClientId());
    config.setCredentials(Map.of("secret", kcReg.getClientSecret()));
    return new PolicyEnforcerFilter(request -> config, "/public/**");
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
