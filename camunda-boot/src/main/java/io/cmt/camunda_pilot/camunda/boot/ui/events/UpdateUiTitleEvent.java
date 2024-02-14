package io.cmt.camunda_pilot.camunda.boot.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import javax.annotation.ParametersAreNonnullByDefault;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ParametersAreNonnullByDefault
public class UpdateUiTitleEvent extends ComponentEvent<Component> {

  /**
   * Creates a new event using the given source and indicator whether the event originated from the
   * client side or the server side.
   *
   * @param source the source component
   */
  public UpdateUiTitleEvent(Component source) {
    super(source, false);
  }
}
