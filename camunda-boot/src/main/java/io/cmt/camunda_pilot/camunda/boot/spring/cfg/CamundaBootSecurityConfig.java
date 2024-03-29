package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.vaadin.flow.spring.security.NavigationAccessControlConfigurer;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import io.cmt.camunda_pilot.camunda.boot.spring.security.KcLogoutHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.webapp.impl.security.auth.ContainerBasedAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

/** Spring Security Config. */
@Configuration
@ComponentScan(basePackageClasses = {KcLogoutHandler.class})
@EnableWebSecurity
@ParametersAreNonnullByDefault
public class CamundaBootSecurityConfig extends VaadinWebSecurity {

  private static final Logger logger = LogManager.getLogger(CamundaBootSecurityConfig.class);

  private static final String REALM_ACCESS_CLAIM = "realm_access";
  private static final String ROLES_CLAIM = "roles";
  private static final String SCOPE_CLAIM = "scope";

  @Nonnull private final LogoutHandler logoutHandler;

  public CamundaBootSecurityConfig(LogoutHandler logoutHandler) {
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
    http.csrf(
            csrf ->
                csrf.ignoringRequestMatchers(
                    antMatcher("/camunda/api/**"), antMatcher("/camunda/engine-rest/**")))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/", "/logout")
                    .permitAll()
                    .requestMatchers("/camunda/**")
                    .hasAuthority("SUPERUSER"))
        .oauth2ResourceServer(cfg -> cfg.jwt(Customizer.withDefaults()))
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
                  .stream().map(SimpleGrantedAuthority::new).toList();
          mappedAuthorities.addAll(roles);
        }
        mappedAuthorities.add(authority);
      }
      return mappedAuthorities;
    };
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Bean
  public FilterRegistrationBean containerBasedAuthenticationFilter() {

    FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
    filterRegistration.setFilter(new ContainerBasedAuthenticationFilter());
    filterRegistration.setInitParameters(
        Collections.singletonMap(
            "authentication-provider",
            "io.cmt.camunda_pilot.camunda.boot.spring.security.KcCamundaAuthenticationProvider"));
    filterRegistration.setOrder(
        201); // make sure the filter is registered after the Spring Security Filter Chain
    filterRegistration.addUrlPatterns("/camunda/*");
    return filterRegistration;
  }
}
