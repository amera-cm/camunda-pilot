package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.trade_acknowledge;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.ASSETS_COUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.DISCOUNT_AMOUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_AMOUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_APPROVAL_STATUS_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_ACKNOWLEDGE_STATUS_VAR_KEY;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import io.cmt.camunda_pilot.camunda.boot.ops.TradeAcknowledgeUserTasksOps;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TradeAcknowledgeStatus;
import io.cmt.camunda_pilot.camunda.boot.ops.UserTaskCmd;
import io.cmt.camunda_pilot.camunda.boot.ui.model.ProcessTask;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.variable.value.DoubleValue;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.context.ApplicationContext;

@ParametersAreNonnullByDefault
public class SetTradeAcknowledgeStatusFormFactory extends BaseTradeAcknowledgeFormFactory {

  @Nonnull
  @Override
  public String taskDefinitionKey() {
    return "setTradeAcknowledgeStatus";
  }

  @Nonnull
  @Override
  public Component create(
      Object payload, ApplicationContext applicationContext, Function<Void, Void> afterCompleteFn) {
    final var task = (ProcessTask) payload;
    final var taskService = applicationContext.getBean(TaskService.class);
    final var userTasksOps = applicationContext.getBean(TradeAcknowledgeUserTasksOps.class);

    final var layout = new VerticalLayout();
    layout.addClassName("taskFormPanel");
    layout.add(new H3(task.getName()));
    layout.add(new Paragraph(task.getProcessDefinitionName()));
    layout.add(new Hr());

    // Task Variables
    Optional<DoubleValue> nominationAmountVar =
        Optional.ofNullable(taskService.getVariableTyped(task.getId(), NOMINATION_AMOUNT_VAR_KEY));
    Optional<IntegerValue> assetsCountVar =
        Optional.ofNullable(taskService.getVariableTyped(task.getId(), ASSETS_COUNT_VAR_KEY));
    Optional<StringValue> approvalStatusVar =
        Optional.ofNullable(
            taskService.getVariableTyped(task.getId(), NOMINATION_APPROVAL_STATUS_VAR_KEY));
    Optional<DoubleValue> discountVar =
        Optional.ofNullable(taskService.getVariableTyped(task.getId(), DISCOUNT_AMOUNT_VAR_KEY));
    Optional<StringValue> acknowledgeStatusVar =
        Optional.ofNullable(
            taskService.getVariableTyped(task.getId(), TRADE_ACKNOWLEDGE_STATUS_VAR_KEY));

    // BEGIN FormLayout
    final var formLayout = new FormLayout();

    final var amount = new NumberField("Nomination amount");
    amount.setEnabled(false);
    nominationAmountVar.ifPresent(av -> amount.setValue(av.getValue()));
    final var dollarPrefix = new Div();
    dollarPrefix.setText("$");
    amount.setPrefixComponent(dollarPrefix);
    formLayout.add(amount);

    final var assetsCount = new NumberField("Assets count");
    assetsCount.setEnabled(false);
    assetsCountVar.ifPresent(av -> assetsCount.setValue(av.getValue().doubleValue()));
    formLayout.add(assetsCount);

    final var approvalStatus =
        new ComboBox<TradesOps.NominationApprovalStatus>("Nomination approval status");
    approvalStatus.setEnabled(false);
    approvalStatus.setItems(Arrays.asList(TradesOps.NominationApprovalStatus.values()));
    approvalStatus.setItemLabelGenerator(TradesOps.NominationApprovalStatus::name);
    approvalStatusVar.ifPresent(
        as ->
            approvalStatus.setValue(
                TradesOps.NominationApprovalStatus.valueOf(
                    as.getValue().toUpperCase(Locale.ROOT))));
    formLayout.add(approvalStatus);

    final var discount = new NumberField("Discount amount");
    discount.setEnabled(false);
    discountVar.ifPresent(av -> discount.setValue(av.getValue()));
    discount.setPrefixComponent(dollarPrefix);
    formLayout.add(discount);

    final var acknowledgeStatus =
        new ComboBox<TradesOps.TradeAcknowledgeStatus>("Acknowledge status");
    acknowledgeStatus.setItems(Arrays.asList(TradesOps.TradeAcknowledgeStatus.values()));
    acknowledgeStatus.setItemLabelGenerator(TradesOps.TradeAcknowledgeStatus::name);
    acknowledgeStatusVar.ifPresent(
        as -> acknowledgeStatus.setValue(TradeAcknowledgeStatus.fromValue(as.getValue())));
    formLayout.add(acknowledgeStatus);

    layout.add(formLayout);
    // END FormLayout

    // BEGIN Buttons Layout
    final var buttonsLayout = new HorizontalLayout();
    final var save = new Button("Save");
    save.addClickListener(
        saveListener(acknowledgeStatus, task, userTasksOps, UserTaskCmd.SAVE, afterCompleteFn));
    final var complete = new Button("Complete");
    complete.addClickListener(
        saveListener(acknowledgeStatus, task, userTasksOps, UserTaskCmd.COMPLETE, afterCompleteFn));
    complete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    buttonsLayout.add(complete, save);
    layout.add(buttonsLayout);
    // END Buttons Layout

    return layout;
  }

  private ComponentEventListener<ClickEvent<Button>> saveListener(
      ComboBox<TradesOps.TradeAcknowledgeStatus> acknowledgeStatus,
      ProcessTask task,
      TradeAcknowledgeUserTasksOps userTasksOps,
      UserTaskCmd cmd,
      Function<Void, Void> afterCompleteFn) {
    return e -> {
      if (acknowledgeStatus.isEmpty()) {
        showWarning("The field acknowledge status is required.");
      } else {
        userTasksOps.setTradeAcknowledgeStatus(task.getId(), acknowledgeStatus.getValue(), cmd);

        if (cmd == UserTaskCmd.SAVE) {
          showSuccess("Task saved.");
        } else if (cmd == UserTaskCmd.COMPLETE) {
          showSuccess("Task completed.");
          afterCompleteFn.apply(null);
        }
      }
    };
  }
}
