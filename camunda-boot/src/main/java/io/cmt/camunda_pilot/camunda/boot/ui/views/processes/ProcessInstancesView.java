package io.cmt.camunda_pilot.camunda.boot.ui.views.processes;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.ProcessEngUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.mixins.WithViewTitle;
import jakarta.annotation.security.RolesAllowed;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.history.HistoricProcessInstance;

@RolesAllowed("USER")
@Route(value = "instances", layout = ProcessTabsRouterLayout.class)
public class ProcessInstancesView extends VerticalLayout implements WithViewTitle {

  private static final Logger logger = LogManager.getLogger(ProcessInstancesView.class);

  private final ProcessEngUiAdapter processEng;
  private final SecurityUiAdapter securityAdapter;

  public ProcessInstancesView(ProcessEngUiAdapter processEng, SecurityUiAdapter securityAdapter) {
    this.processEng = processEng;
    this.securityAdapter = securityAdapter;
    final var h2 = new H2(getViewTitle());
    add(h2);

    final var grid = new Grid<>(HistoricProcessInstance.class, false);
    setupGrid(grid);
    add(grid);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("ProcessInstancesView::onAttach");
    updateUiTitle(attachEvent.getUI());
  }

  @Nonnull
  @Override
  public String getViewTitle() {
    return "Process Instances";
  }

  private void setupGrid(Grid<HistoricProcessInstance> grid) {
    grid.addColumn(
            new TextRenderer<>(
                pi ->
                    "%s (%s:%s)"
                        .formatted(
                            pi.getProcessDefinitionName(),
                            pi.getProcessDefinitionKey(),
                            pi.getProcessDefinitionVersion())))
        .setHeader("Process Name (key:version)");
    //    grid.addColumn(HistoricProcessInstance::getProcessDefinitionKey).setHeader("Proc. Key");
    //    grid.addColumn(HistoricProcessInstance::getProcessDefinitionVersion).setHeader("Version");
    grid.addColumn(HistoricProcessInstance::getState).setHeader("State");
    grid.addColumn(HistoricProcessInstance::getStartTime).setHeader("Started");
    grid.addColumn(HistoricProcessInstance::getEndTime).setHeader("Ended");
    //    grid.addColumn(HistoricProcessInstance::getStartUserId).setHeader("By");

    grid.setItems(processEng.historicProcessInstances());
  }
}
