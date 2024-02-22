package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.user_tasks.trade_acknowledge;

import io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.BaseUserTaskCreateListener;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.PilotProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.ops.UsersOps;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component(
    PilotProcessDefinitionKey.Keys.TRADE_ACKNOWLEDGE_KEY
        + SetTradeAcknowledgeStatusCreateListener.TASK_DEFINITION_KEY)
@ParametersAreNonnullByDefault
public class SetTradeAcknowledgeStatusCreateListener extends BaseUserTaskCreateListener {

  public static final String TASK_DEFINITION_KEY = "setTradeAcknowledgeStatus";

  private final UsersOps usersOps;

  public SetTradeAcknowledgeStatusCreateListener(UsersOps usersOps) {
    this.usersOps = usersOps;
  }

  @Nonnull
  @Override
  public ProcessDefinitionKey processDefinitionKey() {
    return PilotProcessDefinitionKey.TRADE_ACKNOWLEDGE;
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
    final var role = "BUYER";
    final var users = usersOps.roleUserMembers(role);
    if (users.isEmpty()) {
      return List.of();
    } else {
      return users.stream().map(UserRepresentation::getId).toList();
    }
  }
}
