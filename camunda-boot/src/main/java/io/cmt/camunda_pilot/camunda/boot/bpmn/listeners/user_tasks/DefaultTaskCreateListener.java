package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.user_tasks;

import io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.UserTaskListenerFinder;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class DefaultTaskCreateListener implements TaskListener {
  private static final Logger logger = LogManager.getLogger(DefaultTaskCreateListener.class);

  private final ApplicationContext applicationContext;
  private final RepositoryService repositoryService;

  public DefaultTaskCreateListener(
      ApplicationContext applicationContext, RepositoryService repositoryService) {
    this.applicationContext = applicationContext;
    this.repositoryService = repositoryService;
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    logger.debug("DefaultTaskCreateListener::notify");
    final var processDef = getProcessDefinition(delegateTask.getProcessDefinitionId());
    final var processDefinitionKey = processDef.getKey();
    final var taskDefinitionKey = delegateTask.getTaskDefinitionKey();

    final var finder = applicationContext.getBean(UserTaskListenerFinder.class);
    finder.findCreateListener(processDefinitionKey, taskDefinitionKey).notify(delegateTask);
  }

  private ProcessDefinition getProcessDefinition(String id) {
    return repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
  }
}
