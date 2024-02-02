package io.cmt.camunda_pilot.boot.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ParametersAreNonnullByDefault
public class UpdateRootHeadViewTitleEvent extends ComponentEvent<Component> {

  @Nonnull String title;

  /**
   * Creates a new event using the given source and indicator whether the event originated from the
   * client side or the server side.
   *
   * @param source the source component
   */
  public UpdateRootHeadViewTitleEvent(Component source, String title) {
    super(source, false);
    this.title = title;
  }
}
