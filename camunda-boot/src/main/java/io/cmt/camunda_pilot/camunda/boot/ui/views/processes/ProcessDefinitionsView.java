package io.cmt.camunda_pilot.camunda.boot.ui.views.processes;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.cmt.camunda_pilot.camunda.boot.events.model.TradeRequestedEvent;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.ProcessEngUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.mixins.WithViewTitle;
import io.cmt.camunda_pilot.camunda.boot.ui.model.ProcessDef;
import jakarta.annotation.security.RolesAllowed;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

@RolesAllowed("USER")
@Route(value = "definitions", layout = ProcessTabsRouterLayout.class)
public class ProcessDefinitionsView extends VerticalLayout implements WithViewTitle {

  private static final Logger logger = LogManager.getLogger(ProcessDefinitionsView.class);

  private final ProcessEngUiAdapter processEng;
  private final SecurityUiAdapter securityAdapter;
  private final ApplicationEventPublisher eventPublisher;

  public ProcessDefinitionsView(
      ProcessEngUiAdapter processEng,
      SecurityUiAdapter securityAdapter,
      ApplicationEventPublisher eventPublisher) {
    this.processEng = processEng;
    this.securityAdapter = securityAdapter;
    this.eventPublisher = eventPublisher;
    final var h2 = new H2(getViewTitle());
    add(h2);

    final var menuBar = createMenuBar();
    add(menuBar);

    final var grid = new Grid<>(ProcessDef.class, false);
    setupGrid(grid);
    add(grid);

    final var gridCtxmenu = new GridContextMenu<>(grid);
    final var start =
        gridCtxmenu.addItem(
            "Start process instance",
            e -> {
              final var dialog = createStartProcessDialog(e.getItem().get().getKey());
              dialog.open();
            });
    start.addComponentAsFirst(createIcon(VaadinIcon.START_COG));
  }

  private MenuBar createMenuBar() {
    final var menuBar = new MenuBar();
    menuBar.addThemeVariants(MenuBarVariant.LUMO_END_ALIGNED);

    menuBar.addItem(
        "Publish a trade requested event",
        e -> {
          eventPublisher.publishEvent(new TradeRequestedEvent());
          showSuccess("Event published!");
        });

    return menuBar;
  }

  private static Component createIcon(VaadinIcon vaadinIcon) {
    Icon icon = vaadinIcon.create();
    icon.getStyle()
        .set("color", "var(--lumo-secondary-text-color)")
        .set("margin-inline-end", "var(--lumo-space-s")
        .set("padding", "var(--lumo-space-xs");
    return icon;
  }

  private Dialog createStartProcessDialog(String processDefinitionKey) {
    final var dialog = new Dialog();
    dialog.setHeaderTitle("Start process instance of %s".formatted(processDefinitionKey));

    final var startBtn = new Button("Start");
    startBtn.setEnabled(false);
    startBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final var cancelBtn = new Button("Cancel");

    final var bizzKeyFld = new TextField("Business key");
    bizzKeyFld.addValueChangeListener(e -> startBtn.setEnabled(e.getValue().length() > 5));

    final var dialogLayout = new VerticalLayout(bizzKeyFld);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
    dialog.add(dialogLayout);

    startBtn.addClickListener(
        e -> {
          final var bizzKey = bizzKeyFld.getValue();
          final var startedBy = securityAdapter.getUser().getName();
          final var variables = Map.<String, Object>of("startedBy", startedBy);
          processEng.startProcessInstance(processDefinitionKey, bizzKey, variables);
          showSuccess("The process instance was started");
          dialog.close();
        });

    cancelBtn.addClickListener(e -> dialog.close());

    dialog.getFooter().add(cancelBtn);
    dialog.getFooter().add(startBtn);

    return dialog;
  }

  private void showSuccess(String message) {
    final var notification = new Notification(message, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    notification.open();
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("ProcessDefinitionsView::onAttach");
    updateUiTitle(attachEvent.getUI());
  }

  @Nonnull
  @Override
  public String getViewTitle() {
    return "Process Definitions";
  }

  private void setupGrid(Grid<ProcessDef> grid) {
    grid.addColumn(ProcessDef::getId).setTooltipGenerator(ProcessDef::getId).setHeader("ID");
    grid.addColumn(ProcessDef::getKey).setHeader("Key");
    grid.addColumn(ProcessDef::getName).setHeader("Name");
    grid.addColumn(ProcessDef::getVersion).setHeader("Version");
    grid.addColumn(ProcessDef::getDeploymentId)
        .setTooltipGenerator(ProcessDef::getDeploymentId)
        .setHeader("Deploy Id.");
    grid.addColumn(ProcessDef::getResourceName).setHeader("Resource Name");

    grid.setItems(processEng.processDefinitions().stream().map(ProcessDef::new).toList());
  }

  private Button kebabButton() {
    final var kebabButton = new Button(new Icon(VaadinIcon.ELLIPSIS_DOTS_V));
    kebabButton.addThemeVariants(ButtonVariant.LUMO_ICON);
    return kebabButton;
  }
}
