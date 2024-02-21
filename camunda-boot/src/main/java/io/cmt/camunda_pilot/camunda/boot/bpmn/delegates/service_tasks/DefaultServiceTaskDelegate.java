package io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.service_tasks;

import io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.ActivityDelegateFinder;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class DefaultServiceTaskDelegate implements JavaDelegate {

  private static final Logger logger = LogManager.getLogger(DefaultServiceTaskDelegate.class);

  private final ActivityDelegateFinder finder;
  private final RepositoryService repositoryService;

  public DefaultServiceTaskDelegate(
      ActivityDelegateFinder finder, RepositoryService repositoryService) {
    this.finder = finder;
    this.repositoryService = repositoryService;
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    final var procDef =
        repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionId(delegateExecution.getProcessDefinitionId())
            .singleResult();
    final var procDefKey = procDef.getKey();
    final var activityId = delegateExecution.getCurrentActivityId();
    logger.debug(() -> "Service task delegate -> %s::%s".formatted(procDefKey, activityId));
    final var delegate = finder.find(procDefKey, activityId);
    delegate.execute(delegateExecution);
  }
}
