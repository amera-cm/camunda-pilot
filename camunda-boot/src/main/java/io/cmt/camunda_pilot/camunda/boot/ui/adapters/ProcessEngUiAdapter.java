package io.cmt.camunda_pilot.camunda.boot.ui.adapters;

import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;

@Component
public class ProcessEngUiAdapter {

  private final RepositoryService repositoryService;
  private final RuntimeService runtimeService;
  private final HistoryService historyService;
  private final TaskService taskService;

  public ProcessEngUiAdapter(
      RepositoryService repositoryService,
      RuntimeService runtimeService,
      HistoryService historyService,
      TaskService taskService) {
    this.repositoryService = repositoryService;
    this.runtimeService = runtimeService;
    this.historyService = historyService;
    this.taskService = taskService;
  }

  public List<ProcessDefinition> processDefinitions() {
    final var query = repositoryService.createProcessDefinitionQuery().active().latestVersion();
    return query.unlimitedList();
  }

  public List<ProcessDefinition> allProcessDefinitions() {
    final var query = repositoryService.createProcessDefinitionQuery();
    return query.unlimitedList();
  }

  public ProcessInstance startProcessInstance(
      String processDefinitionKey, String businessKey, Map<String, Object> variables) {
    return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
  }

  public List<ProcessInstance> rootProcessInstances() {
    final var query = runtimeService.createProcessInstanceQuery().rootProcessInstances();
    return query.unlimitedList();
  }

  public List<HistoricProcessInstance> historicProcessInstances() {
    final var query =
        historyService
            .createHistoricProcessInstanceQuery()
            .rootProcessInstances()
            .orderByProcessInstanceStartTime()
            .desc();
    return query.unlimitedList();
  }

  public Map<String, Object> taskVariables(String taskId) {
    return taskService.getVariables(taskId);
  }

  public List<Task> tasksForUserId(String userId) {
    final var query =
        taskService.createTaskQuery().taskAssignee(userId).active().orderByTaskCreateTime().asc();
    return query.unlimitedList();
  }

  public void saveTask(String taskId, Map<String, Object> variables) {
    taskService.setVariables(taskId, variables);
  }

  public void completeTask(String taskId, Map<String, Object> variables) {
    taskService.complete(taskId, variables);
  }
}
