package io.cmt.camunda_pilot.boot.spring;

import com.vaadin.flow.spring.security.NavigationAccessControlConfigurer;
import io.cmt.camunda_pilot.boot.spring.security.KeycloakLogoutHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

// @Configuration
// @ComponentScan(basePackageClasses = {KeycloakLogoutHandler.class})
// @EnableWebSecurity
@ParametersAreNonnullByDefault
public class BootUiSecurityConfig {

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String SCOPE_CLAIM = "scope";

  @Nonnull private final LogoutHandler logoutHandler;

  public BootUiSecurityConfig(LogoutHandler logoutHandler) {
    this.logoutHandler = logoutHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .oauth2ResourceServer(
            cfg -> cfg.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter())))
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout.addLogoutHandler(logoutHandler).logoutSuccessUrl("/"));
    //        .addFilterAfter(
    //            createPolicyEnforcerFilter(configData, kgReg),
    // BearerTokenAuthenticationFilter.class);
    return http.build();
  }

//  @Bean
//  NavigationAccessControlConfigurer navigationAccessControlConfigurer() {
//    return new NavigationAccessControlConfigurer()
//        .withRoutePathAccessChecker();
//  }

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
