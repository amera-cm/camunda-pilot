package io.cmt.camunda_pilot.camunda.boot.ui.model;

import javax.annotation.ParametersAreNonnullByDefault;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.camunda.bpm.engine.repository.ProcessDefinition;

@Value
@AllArgsConstructor
@ParametersAreNonnullByDefault
public class ProcessDef {
  String id;
  String key;
  int version;
  String name;
  String category;
  String deploymentId;
  String resourceName;
  boolean suspended;
  String versionTag;

  public ProcessDef(ProcessDefinition definition) {
    this(
        definition.getId(),
        definition.getKey(),
        definition.getVersion(),
        definition.getName(),
        definition.getCategory(),
        definition.getDeploymentId(),
        definition.getResourceName(),
        definition.isSuspended(),
        definition.getVersionTag());
  }
}
