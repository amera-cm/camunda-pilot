package io.cmt.camunda_pilot.camunda.boot.bpmn.external_tasks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;

@ParametersAreNonnullByDefault
public interface ExternalTaskHandler {

  @Nonnull
  String topic();

  void handle(String workerId, LockedExternalTask task);
}
