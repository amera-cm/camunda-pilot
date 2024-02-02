package io.cmt.camunda_pilot.boot.ui.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import io.cmt.camunda_pilot.boot.ui.events.UpdateRootHeadViewTitleEvent;
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
    ComponentUtil.addListener(
        attachEvent.getUI(), UpdateRootHeadViewTitleEvent.class, this::updateTitleListener);
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    logger.info(event.getLocation().getPathWithQueryParameters());
  }

  private void updateTitleListener(UpdateRootHeadViewTitleEvent event) {
    logger.info("RootHeadViewTitle -> updateTitleListener");
    setText(event.getTitle());
  }
}
