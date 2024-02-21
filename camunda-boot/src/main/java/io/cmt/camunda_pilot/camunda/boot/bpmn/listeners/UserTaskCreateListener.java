package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateTask;

@ParametersAreNonnullByDefault
public interface UserTaskCreateListener extends UserTaskListener {

  Logger logger = LogManager.getLogger(UserTaskCreateListener.class);

  @Nonnull
  List<String> findUserCandidates(DelegateTask delegateTask);

  default void assignCandidates(DelegateTask delegateTask) {
    final var candidates = findUserCandidates(delegateTask);
    if (candidates.isEmpty()) {
      logger.error(
          () ->
              "No task candidates found for: %s::%s"
                  .formatted(processDefinitionKey().key(), taskDefinitionKey()));
    } else {
      logger.debug(
          () ->
              "Task candidates found for: %s::%s -> %s"
                  .formatted(processDefinitionKey().key(), taskDefinitionKey(), candidates));
      if (candidates.size() == 1) {
        delegateTask.setAssignee(candidates.get(0));
      } else {
        delegateTask.addCandidateUsers(candidates);
      }
    }
  }
}
