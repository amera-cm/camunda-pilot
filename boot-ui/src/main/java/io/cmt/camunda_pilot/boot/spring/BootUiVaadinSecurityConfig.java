package io.cmt.camunda_pilot.boot.spring;

import com.vaadin.flow.spring.security.NavigationAccessControlConfigurer;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import io.cmt.camunda_pilot.boot.spring.security.KeycloakLogoutHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@ComponentScan(basePackageClasses = {KeycloakLogoutHandler.class})
@EnableWebSecurity
@ParametersAreNonnullByDefault
public class BootUiVaadinSecurityConfig extends VaadinWebSecurity {

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String SCOPE_CLAIM = "scope";

  @Nonnull private final LogoutHandler logoutHandler;

  public BootUiVaadinSecurityConfig(LogoutHandler logoutHandler) {
    this.logoutHandler = logoutHandler;
  }

  @Bean
  public static NavigationAccessControlConfigurer navigationAccessControlConfigurer() {
    return new NavigationAccessControlConfigurer().withAnnotatedViewAccessChecker();
  }

  @Bean
  public static GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    //        .requestMatchers(antMatchers("/ui/dashboard/**"))
    //        .hasAuthority("USER_PREMIUM")
    //                    .requestMatchers(antMatchers("/ui/**"))
    //        .hasAuthority("USER")

    http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/logout").permitAll());
    http.oauth2ResourceServer(cfg -> cfg.jwt(Customizer.withDefaults()))
        .oauth2Login(Customizer.withDefaults());
    super.configure(http);
    setOAuth2LoginPage(http, "/oauth2/authorization/keycloak");
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

  //  @ParametersAreNonnullByDefault
  //  private static class JwtAuthenticationConverter
  //      implements Converter<Jwt, AbstractAuthenticationToken> {
  //    @SuppressWarnings("unchecked")
  //    @Nonnull
  //    private static List<SimpleGrantedAuthority> jwtToGrantedAuthorities(Jwt jwt) {
  //      final var authorities = new ArrayList<SimpleGrantedAuthority>();
  //      if (jwt.hasClaim(REALM_ACCESS_CLAIM)) {
  //        final var realmAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
  //        final var roles = (List<String>) realmAccess.get(ROLES_CLAIM);
  //        authorities.addAll(fromAuthNames(roles, "ROLE_"));
  //      }
  //      if (jwt.hasClaim(SCOPE_CLAIM)) {
  //        final var scopes = Arrays.asList(jwt.getClaimAsString(SCOPE_CLAIM).split(" "));
  //        authorities.addAll(fromAuthNames(scopes, "SCOPE_"));
  //      }
  //      return authorities;
  //    }
  //
  //    @Nonnull
  //    private static List<SimpleGrantedAuthority> fromAuthNames(
  //        List<String> authNames, @Nullable String prefix) {
  //      final var _prefix = Objects.requireNonNullElse(prefix, "");
  //      return authNames.stream()
  //          .map(name -> new SimpleGrantedAuthority(_prefix + name.toUpperCase(Locale.ROOT)))
  //          .toList();
  //    }
  //
  //    @Nonnull
  //    public AbstractAuthenticationToken convert(Jwt jwt) {
  //      return new JwtAuthenticationToken(jwt, jwtToGrantedAuthorities(jwt));
  //    }
  //  }
}
