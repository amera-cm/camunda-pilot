package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners;

import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.delegate.DelegateTask;

@ParametersAreNonnullByDefault
public abstract class BaseUserTaskCreateListener implements UserTaskCreateListener {
  @Override
  public void notify(DelegateTask delegateTask) {
    assignCandidates(delegateTask);
  }
}
