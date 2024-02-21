package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.trade_nomination;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.ASSETS_COUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_AMOUNT_VAR_KEY;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import io.cmt.camunda_pilot.camunda.boot.ops.TradeNominationUserTasksOps;
import io.cmt.camunda_pilot.camunda.boot.ops.UserTaskCmd;
import io.cmt.camunda_pilot.camunda.boot.ui.model.ProcessTask;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.variable.value.DoubleValue;
import org.camunda.bpm.engine.variable.value.IntegerValue;
import org.springframework.context.ApplicationContext;

@ParametersAreNonnullByDefault
public class SetupNominationFormFactory extends BaseTradeNominationFormFactory {

  @Nonnull
  @Override
  public String taskDefinitionKey() {
    return "setupNomination";
  }

  @Nonnull
  @Override
  public Component create(
      Object payload, ApplicationContext applicationContext, Function<Void, Void> afterCompleteFn) {
    final var task = (ProcessTask) payload;
    final var taskService = applicationContext.getBean(TaskService.class);
    final var userTasksOps = applicationContext.getBean(TradeNominationUserTasksOps.class);

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

    // BEGIN FormLayout
    final var formLayout = new FormLayout();

    final var amount = new NumberField("Nomination amount");
    nominationAmountVar.ifPresent(av -> amount.setValue(av.getValue()));
    final var dollarPrefix = new Div();
    dollarPrefix.setText("$");
    amount.setPrefixComponent(dollarPrefix);
    formLayout.add(amount);

    final var assetsCount = new NumberField("Assets count");
    assetsCountVar.ifPresent(av -> assetsCount.setValue(av.getValue().doubleValue()));
    formLayout.add(assetsCount);
    layout.add(formLayout);
    // END FormLayout

    // BEGIN Buttons Layout
    final var buttonsLayout = new HorizontalLayout();
    final var save = new Button("Save");
    save.addClickListener(
        saveListener(amount, assetsCount, task, userTasksOps, UserTaskCmd.SAVE, afterCompleteFn));
    final var complete = new Button("Complete");
    complete.addClickListener(
        saveListener(
            amount, assetsCount, task, userTasksOps, UserTaskCmd.COMPLETE, afterCompleteFn));
    complete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    buttonsLayout.add(complete, save);
    layout.add(buttonsLayout);
    // END Buttons Layout

    return layout;
  }

  private ComponentEventListener<ClickEvent<Button>> saveListener(
      NumberField amount,
      NumberField assetsCount,
      ProcessTask task,
      TradeNominationUserTasksOps userTasksOps,
      UserTaskCmd cmd,
      Function<Void, Void> afterCompleteFn) {
    return e -> {
      if (amount.isEmpty() || assetsCount.isEmpty()) {
        showWarning("The fields nomination amount and assets count are required.");
      } else {
        userTasksOps.setupNomination(
            task.getId(), amount.getValue(), assetsCount.getValue().intValue(), cmd);

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
