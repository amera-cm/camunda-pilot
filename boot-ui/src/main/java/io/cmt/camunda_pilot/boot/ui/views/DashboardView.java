package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.cmt.camunda_pilot.boot.ui.events.UpdateRootHeadViewTitleEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Route(value = "", layout = DashboardRouterLayout.class)
public class DashboardView extends VerticalLayout {

  private static final Logger logger = LogManager.getLogger(DashboardView.class);

  public DashboardView() {
    final var h1 = new H1("Dashboard (all users)");
    add(h1);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("DashboardView onAttach");
    ComponentUtil.fireEvent(
        attachEvent.getUI(), new UpdateRootHeadViewTitleEvent(this, "Dashboard -All users"));
  }
}
