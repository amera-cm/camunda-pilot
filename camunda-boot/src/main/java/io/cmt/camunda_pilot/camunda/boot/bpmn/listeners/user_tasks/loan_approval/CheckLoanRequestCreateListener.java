package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.user_tasks.loan_approval;

import io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.BaseUserTaskCreateListener;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.PilotProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.stereotype.Component;

@Component(
    PilotProcessDefinitionKey.Keys.LOAN_APPROVAL_KEY
        + CheckLoanRequestCreateListener.TASK_DEFINITION_KEY)
@ParametersAreNonnullByDefault
public class CheckLoanRequestCreateListener extends BaseUserTaskCreateListener {

  public static final String TASK_DEFINITION_KEY = "checkLoanRequest";

  @Nonnull
  @Override
  public ProcessDefinitionKey processDefinitionKey() {
    return PilotProcessDefinitionKey.LOAN_APPROVAL;
  }

  @Nonnull
  @Override
  public String taskDefinitionKey() {
    return TASK_DEFINITION_KEY;
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    super.notify(delegateTask);
  }

  @Nonnull
  @Override
  public List<String> findUserCandidates(DelegateTask delegateTask) {
    if (delegateTask.hasVariable("startedBy")) {
      final StringValue assignee = delegateTask.getVariableTyped("startedBy");
      return List.of(assignee.getValue());
    } else {
      return List.of();
    }
  }
}
