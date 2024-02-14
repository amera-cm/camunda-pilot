package io.cmt.camunda_pilot.camunda.boot.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.mixins.WithViewTitle;
import jakarta.annotation.security.RolesAllowed;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@RolesAllowed("USER")
@Route(value = "", layout = HomeTabsRouterLayout.class)
public class HomeUserView extends VerticalLayout implements WithViewTitle {

  private static final Logger logger = LogManager.getLogger(HomeUserView.class);
  private final SecurityUiAdapter securityAdapter;

  public HomeUserView(AuthenticationContext authContext, SecurityUiAdapter securityAdapter) {
    this.securityAdapter = securityAdapter;
    final var h1 = new H1(getViewTitle());
    logger.info(authContext.getAuthenticatedUser(OidcUser.class));
    add(h1);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("HomeUserView::onAttach");
    updateUiTitle(attachEvent.getUI());
  }

  @Nonnull
  @Override
  public String getViewTitle() {
    return "Home for USERs";
  }
}
