package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners;

import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.delegate.TaskListener;

@ParametersAreNonnullByDefault
public interface UserTaskListener extends TaskListener {
  @Nonnull
  ProcessDefinitionKey processDefinitionKey();

  @Nonnull
  String taskDefinitionKey();
}
