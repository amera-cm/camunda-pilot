package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.ProcessEngUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.mixins.WithViewTitle;
import io.cmt.camunda_pilot.camunda.boot.ui.model.ProcessTask;
import io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.FormFactoryFinder;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

@RolesAllowed("USER")
@Route(value = "my", layout = TasksTabsRouterLayout.class)
public class MyTasksView extends SplitLayout implements WithViewTitle {

  private static final Logger logger = LogManager.getLogger(MyTasksView.class);

  private final ProcessEngUiAdapter processEng;
  private final SecurityUiAdapter securityAdapter;
  private final ApplicationContext applicationContext;

  private final Grid<ProcessTask> grid;
  private final VerticalLayout taskListPanel;
  private final VerticalLayout emptyTaskFormPanel;

  public MyTasksView(
      ProcessEngUiAdapter processEng,
      SecurityUiAdapter securityAdapter,
      ApplicationContext applicationContext) {
    this.processEng = processEng;
    this.securityAdapter = securityAdapter;
    this.applicationContext = applicationContext;
    this.grid = new Grid<>(ProcessTask.class, false);
    this.taskListPanel = createTaskListPanel();
    this.emptyTaskFormPanel = createEmptyTaskFormPanel();
    addToPrimary(this.taskListPanel);
    addToSecondary(this.emptyTaskFormPanel);
    setSizeFull();
    setSplitterPosition(70);
  }

  private VerticalLayout createTaskListPanel() {
    final var layout = new VerticalLayout();
    final var h2 = new H2(getViewTitle());
    layout.add(h2);

    setupGrid(grid);
    layout.add(grid);
    return layout;
  }

  private VerticalLayout createEmptyTaskFormPanel() {
    final var layout = new VerticalLayout();
    layout.addClassName("emptyTaskFormPanel");
    layout.add(new H3("Please, select a task."));
    return layout;
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    updateUiTitle(attachEvent.getUI());
  }

  @Nonnull
  @Override
  public String getViewTitle() {
    return "My Tasks";
  }

  private void setupGrid(Grid<ProcessTask> grid) {
    grid.addColumn(ProcessTask::getProcessDefinitionName).setHeader("Process");
    grid.addColumn(ProcessTask::getName).setHeader("Task");
    grid.addColumn(ProcessTask::getPriority).setHeader("Priority");
    grid.addColumn(ProcessTask::getCreateTime).setHeader("Created");
    grid.addColumn(ProcessTask::getAssignee).setHeader("Assignee");

    grid.addSelectionListener(
        selection -> {
          final var task = selection.getFirstSelectedItem().orElse(null);
          updateTaskFormPanel(task);
        });

    updateGridData();
  }

  private void updateGridData() {
    grid.setItems(tasksData());
  }

  private void updateTaskFormPanel(@Nullable ProcessTask task) {
    if (task != null) {
      final var finder = new FormFactoryFinder();
      final var factory = finder.find(task.getProcessDefinitionKey(), task.getTaskDefinitionKey());
      if (factory != null) {
        final var form = factory.create(task, applicationContext);
        form.addTaskCompletedListener(
            e -> {
              grid.deselectAll();
              updateGridData();
            });
        addToSecondary(form);
      } else {
        addToSecondary(createDefaultTaskFormPanel(task));
      }
    } else {
      addToSecondary(emptyTaskFormPanel);
    }
    setSplitterPosition(70);
  }

  private VerticalLayout createDefaultTaskFormPanel(ProcessTask task) {
    final var layout = new VerticalLayout();
    layout.addClassName("taskFormPanel");
    layout.add(new H3(task.getName()));
    layout.add(new Paragraph(task.getProcessDefinitionName()));
    layout.add(new Hr());
    // Task Variables
    final var taskVariables = processEng.taskVariables(task.getId());
    final var amountVar = Optional.ofNullable((String) taskVariables.get("amount"));
    final var commentsVar = Optional.ofNullable((String) taskVariables.get("comments"));
    // BEGIN FormLayout
    final var formLayout = new FormLayout();

    final var amount = new NumberField("Approved amount");
    amountVar.ifPresent(av -> amount.setValue(Double.valueOf(av)));
    final var dollarPrefix = new Div();
    dollarPrefix.setText("$");
    amount.setPrefixComponent(dollarPrefix);
    formLayout.add(amount);
    final var comments = new TextArea("Comments");
    commentsVar.ifPresent(comments::setValue);
    formLayout.add(comments);
    layout.add(formLayout);
    // END FormLayout

    // BEGIN Buttons Layout
    final var buttonsLayout = new HorizontalLayout();
    final var save = new Button("Save");
    final var complete = new Button("Complete");
    complete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    save.addClickListener(
        e -> {
          if (amount.isEmpty() || comments.isEmpty()) {
            showWarning("The fields amount and comments are required.");
          } else {
            final var taskVars =
                Map.<String, Object>of(
                    "amount", amount.getValue().toString(), "comments", comments.getValue());
            processEng.saveTask(task.getId(), taskVars);
            showSuccess("The task's variables have been saved.");
          }
        });

    complete.addClickListener(
        e -> {
          if (amount.isEmpty() || comments.isEmpty()) {
            showWarning("The fields amount and comments are required.");
          } else {
            final var taskVars =
                Map.<String, Object>of(
                    "amount", amount.getValue().toString(), "comments", comments.getValue());
            processEng.completeTask(task.getId(), taskVars);
            showSuccess("The task has been completed.");
            grid.deselectAll();
            updateGridData();
          }
        });

    buttonsLayout.add(complete, save);
    layout.add(buttonsLayout);
    // END Buttons Layout
    return layout;
  }

  private void showWarning(String message) {
    //    Notification.show(message, 5000, Notification.Position.MIDDLE);
    final var notification = new Notification(message, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
    notification.open();
  }

  private void showSuccess(String message) {
    final var notification = new Notification(message, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    notification.open();
  }

  private List<ProcessTask> tasksData() {
    final var procDefsList = processEng.allProcessDefinitions();
    final var procDefs =
        procDefsList.stream().collect(Collectors.toMap(pd -> pd.getId(), Function.identity()));
    final var tasks = processEng.tasksForUserId(securityAdapter.getUser().getName());
    return tasks.stream()
        .map(t -> new ProcessTask(t, procDefs.get(t.getProcessDefinitionId())))
        .toList();
  }
}
