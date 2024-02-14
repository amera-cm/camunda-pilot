package io.cmt.camunda_pilot.camunda.boot.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.cmt.camunda_pilot.camunda.boot.ui.mixins.WithViewTitle;
import jakarta.annotation.security.RolesAllowed;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RolesAllowed({"SUPERUSER"})
@Route(value = "superusers", layout = HomeTabsRouterLayout.class)
public class HomeSuperuserView extends VerticalLayout implements WithViewTitle {

  private static final Logger logger = LogManager.getLogger(HomeSuperuserView.class);

  public HomeSuperuserView() {
    final var h1 = new H1(getViewTitle());
    add(h1);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("HomeSuperuserView onAttach");
    updateUiTitle(attachEvent.getUI());
  }

  @Nonnull
  @Override
  public String getViewTitle() {
    return "Home for SUPERUSERs";
  }
}
