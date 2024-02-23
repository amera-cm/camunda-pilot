package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.trade_nomination;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.ASSETS_COUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_AMOUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_APPROVAL_STATUS_VAR_KEY;

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
import com.vaadin.flow.component.textfield.NumberField;
import io.cmt.camunda_pilot.camunda.boot.ops.TradeNominationUserTasksOps;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NominationApprovalStatus;
import io.cmt.camunda_pilot.camunda.boot.ops.UserTaskCmd;
import io.cmt.camunda_pilot.camunda.boot.ui.model.ProcessTask;
import io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.UserTaskFormPanel;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.variable.value.DoubleValue;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.context.ApplicationContext;

@ParametersAreNonnullByDefault
public class SetNominationApprovalStatusFormFactory extends BaseTradeNominationFormFactory {

  @Nonnull
  @Override
  public String taskDefinitionKey() {
    return "setNominationApprovalStatus";
  }

  @Nonnull
  @Override
  public UserTaskFormPanel create(Object payload, ApplicationContext applicationContext) {
    final var task = (ProcessTask) payload;
    final var taskService = applicationContext.getBean(TaskService.class);
    final var userTasksOps = applicationContext.getBean(TradeNominationUserTasksOps.class);

    final var layout = new UserTaskFormPanel();
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

    final var approvalStatus = new ComboBox<NominationApprovalStatus>("Nomination approval status");
    approvalStatus.setItems(Arrays.asList(NominationApprovalStatus.values()));
    approvalStatus.setItemLabelGenerator(NominationApprovalStatus::name);
    approvalStatusVar.ifPresent(
        as ->
            approvalStatus.setValue(
                NominationApprovalStatus.valueOf(as.getValue().toUpperCase(Locale.ROOT))));
    formLayout.add(approvalStatus);

    layout.add(formLayout);
    // END FormLayout

    // BEGIN Buttons Layout
    final var buttonsLayout = new HorizontalLayout();
    final var save = new Button("Save");
    save.addClickListener(
        saveListener(approvalStatus, task, userTasksOps, UserTaskCmd.SAVE, layout));
    final var complete = new Button("Complete");
    complete.addClickListener(
        saveListener(approvalStatus, task, userTasksOps, UserTaskCmd.COMPLETE, layout));
    complete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    buttonsLayout.add(complete, save);
    layout.add(buttonsLayout);
    // END Buttons Layout

    return layout;
  }

  private ComponentEventListener<ClickEvent<Button>> saveListener(
      ComboBox<NominationApprovalStatus> approvalStatus,
      ProcessTask task,
      TradeNominationUserTasksOps userTasksOps,
      UserTaskCmd cmd,
      UserTaskFormPanel formPanel) {
    return e -> {
      if (approvalStatus.isEmpty()) {
        showWarning("The field nomination approval status is required.");
      } else {
        userTasksOps.setNominationApprovalStatus(task.getId(), approvalStatus.getValue(), cmd);
        if (cmd == UserTaskCmd.SAVE) {
          showSuccess("Task saved.");
        } else if (cmd == UserTaskCmd.COMPLETE) {
          showSuccess("Task completed. (TaskCompletedEvent)");
          formPanel.fireTaskCompletedEvent();
        }
      }
    };
  }
}
