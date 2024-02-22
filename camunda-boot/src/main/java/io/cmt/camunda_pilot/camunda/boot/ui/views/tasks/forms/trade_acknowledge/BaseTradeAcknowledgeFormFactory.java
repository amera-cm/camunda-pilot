package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.trade_acknowledge;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.PilotProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.FormFactory;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class BaseTradeAcknowledgeFormFactory implements FormFactory {
  @Nonnull
  @Override
  public String processDefinitionKey() {
    return PilotProcessDefinitionKey.TRADE_ACKNOWLEDGE.key();
  }

  protected void showWarning(String message) {
    final var notification = new Notification(message, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
    notification.open();
  }

  protected void showSuccess(String message) {
    final var notification = new Notification(message, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    notification.open();
  }
}
