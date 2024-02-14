package io.cmt.camunda_pilot.camunda.boot.ui.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import io.cmt.camunda_pilot.camunda.boot.ui.events.UpdateUiTitleEvent;
import io.cmt.camunda_pilot.camunda.boot.ui.mixins.WithViewTitle;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class RootHeadViewTitle extends H2 implements AfterNavigationObserver {

  private static final Logger logger = LogManager.getLogger(RootHeadViewTitle.class);

  public RootHeadViewTitle(String text) {
    super(text);
    getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    //    getEventBus().addListener(UpdateRootHeadViewTitleEvent.class, this::updateTitleListener);
    logger.info("RootHeadViewTitle::onAttach");
    ComponentUtil.addListener(
        attachEvent.getUI(), UpdateUiTitleEvent.class, this::updateTitleListener);
    updateUiTitle(attachEvent.getUI());
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    logger.info(event.getLocation().getPathWithQueryParameters());
  }

  private void updateTitleListener(UpdateUiTitleEvent event) {
    logger.info("RootHeadViewTitle -> updateTitleListener");
    updateUiTitle(UI.getCurrent());
  }

  private void updateUiTitle(UI ui) {
    final var session = ui.getSession();
    final var title = session.getAttribute(WithViewTitle.attrKey(ui.getUIId()));
    if (title != null) {
      setText((String) title);
    }
  }
}
