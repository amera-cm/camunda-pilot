package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import io.cmt.camunda_pilot.boot.adapters.SecurityAdapter;
import io.cmt.camunda_pilot.boot.ui.events.UpdateRootHeadViewTitleEvent;
import jakarta.annotation.security.RolesAllowed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@RolesAllowed("USER")
@Route(value = "", layout = DashboardRouterLayout.class)
public class DashboardView extends VerticalLayout {

  private static final Logger logger = LogManager.getLogger(DashboardView.class);
  private final SecurityAdapter securityAdapter;

  public DashboardView(AuthenticationContext authContext, SecurityAdapter securityAdapter) {
    this.securityAdapter = securityAdapter;
    final var h1 = new H1("Dashboard (all users)");
    logger.info(authContext.getAuthenticatedUser(OidcUser.class));
    add(h1);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    ComponentUtil.fireEvent(
        attachEvent.getUI(), new UpdateRootHeadViewTitleEvent(this, "Dashboard -All users"));
  }
}
