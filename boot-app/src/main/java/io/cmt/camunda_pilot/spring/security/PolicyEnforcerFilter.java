package io.cmt.camunda_pilot.spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/** Policy Enforcer Filter. */
@ParametersAreNonnullByDefault
public class PolicyEnforcerFilter extends ServletPolicyEnforcerFilter {

  private static final Logger logger = LogManager.getLogger(PolicyEnforcerFilter.class);

  @Nonnull private final List<PathPattern> ignorePatterns;

  /** All args constructor. */
  public PolicyEnforcerFilter(
      ConfigurationResolver configResolver, @Nullable String... ignorePaths) {
    super(configResolver);
    if (ignorePaths == null) {
      this.ignorePatterns = List.of();
    } else {
      final var patternParser = new PathPatternParser();
      this.ignorePatterns = new ArrayList<>(ignorePaths.length);
      for (String ignorePath : ignorePaths) {
        this.ignorePatterns.add(patternParser.parse(ignorePath));
      }
    }
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    final var request = (HttpServletRequest) servletRequest;
    final var path = request.getRequestURI();
    if (mustIgnore(path)) {
      logger.debug(
          () -> "Path %s is ignored by the security policy enforcer filter.".formatted(path));
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      super.doFilter(servletRequest, servletResponse, filterChain);
    }
  }

  private boolean mustIgnore(String path) {
    final var pathContainer = PathContainer.parsePath(path);
    for (PathPattern ignorePattern : ignorePatterns) {
      if (ignorePattern.matches(pathContainer)) {
        return true;
      }
    }
    return false;
  }
}
