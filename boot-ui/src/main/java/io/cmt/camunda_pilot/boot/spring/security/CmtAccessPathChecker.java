package io.cmt.camunda_pilot.boot.spring.security;

import com.vaadin.flow.server.auth.AccessPathChecker;
import java.security.Principal;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CmtAccessPathChecker implements AccessPathChecker {

  private static final Logger logger = LogManager.getLogger(CmtAccessPathChecker.class);

  private final ApplicationContext applicationContext;

  public CmtAccessPathChecker(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    logger.info("CREATED");
  }

  @Override
  public boolean hasAccess(String path, Principal principal, Predicate<String> roleChecker) {
    //    final HttpServletRequest request =
    // VaadinServletRequest.getCurrent().getHttpServletRequest();
    logger.info(path);
    //    ((AuthorizationManagerWebInvocationPrivilegeEvaluator)applicationContext.getBean(
    //
    // RequestMatcherDelegatingWebInvocationPrivilegeEvaluator.class).delegates.get(0).entry.get(0)).authorizationManager
    return true;
  }
}
