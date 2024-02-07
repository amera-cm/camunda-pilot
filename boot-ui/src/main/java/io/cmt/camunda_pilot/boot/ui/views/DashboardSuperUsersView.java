package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.cmt.camunda_pilot.boot.ui.events.UpdateRootHeadViewTitleEvent;
import jakarta.annotation.security.RolesAllowed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RolesAllowed({"USER_PREMIUM"})
@Route(value = "superusers", layout = DashboardRouterLayout.class)
public class DashboardSuperUsersView extends VerticalLayout {

  private static final Logger logger = LogManager.getLogger(DashboardSuperUsersView.class);

  public DashboardSuperUsersView() {
    final var h1 = new H1("Dashboard for superusers");
    add(h1);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("DashboardSuperUsersView onAttach");
    ComponentUtil.fireEvent(
        attachEvent.getUI(), new UpdateRootHeadViewTitleEvent(this, "Dashboard / Super users"));
  }
}
