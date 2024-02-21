package io.cmt.camunda_pilot.camunda.boot.bpmn.delegates;

import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@ParametersAreNonnullByDefault
public interface ActivityDelegate extends JavaDelegate {

  @Nonnull
  ProcessDefinitionKey processDefinitionKey();

  @Nonnull
  String activityId();
}
