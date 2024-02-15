package io.cmt.camunda_pilot.camunda.boot.listeners;

import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class CreateTaskListener implements TaskListener {
  private static final Logger logger = LogManager.getLogger(CreateTaskListener.class);

  @Override
  public void notify(DelegateTask delegateTask) {
    logger.info("CreateTaskListener::notify");
    if (delegateTask.hasVariable("startedBy")) {
      final var assigne = (String) delegateTask.getVariable("startedBy");
      delegateTask.setAssignee(assigne);
      logger.info("Task %s assigned to %s".formatted(delegateTask.getName(), assigne));
    } else {
      logger.error("Cannot set the assignee to task " + delegateTask.getName());
    }
  }
}
