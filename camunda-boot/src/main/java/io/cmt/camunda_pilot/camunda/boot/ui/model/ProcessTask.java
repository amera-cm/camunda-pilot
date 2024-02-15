package io.cmt.camunda_pilot.camunda.boot.ui.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;

@Value
@AllArgsConstructor
public class ProcessTask {
  String id;
  String taskDefinitionKey;
  String name;
  String description;
  int priority;

  String assignee;
  String owner;

  Date createTime;
  Date lastUpdated;
  Date dueDate;

  String processInstanceId;

  String processDefinitionId;
  String processDefinitionKey;
  String processDefinitionName;

  public ProcessTask(Task task, ProcessDefinition processDefinition) {
    this(
        task.getId(),
        task.getTaskDefinitionKey(),
        task.getName(),
        task.getDescription(),
        task.getPriority(),
        task.getAssignee(),
        task.getOwner(),
        task.getCreateTime(),
        task.getLastUpdated(),
        task.getDueDate(),
        task.getProcessInstanceId(),
        processDefinition.getId(),
        processDefinition.getKey(),
        processDefinition.getName());
  }
}
