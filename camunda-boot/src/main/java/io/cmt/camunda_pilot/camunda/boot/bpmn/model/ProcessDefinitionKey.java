package io.cmt.camunda_pilot.camunda.boot.bpmn.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ProcessDefinitionKey {

  @Nonnull
  String key();
}
