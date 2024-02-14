package io.cmt.camunda_pilot.camunda.boot.ui.mixins;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import io.cmt.camunda_pilot.camunda.boot.ui.events.UpdateUiTitleEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface WithViewTitle {

  static String attrKey(int uiid) {
    return "ui_title_" + uiid;
  }

  @Nonnull
  String getViewTitle();

  default void updateUiTitle(UI ui) {
    final var uiid = ui.getUIId();
    final var attrKey = attrKey(uiid);
    ui.getSession().setAttribute(attrKey, getViewTitle());
    ComponentUtil.fireEvent(ui, new UpdateUiTitleEvent((Component) this));
  }
}
