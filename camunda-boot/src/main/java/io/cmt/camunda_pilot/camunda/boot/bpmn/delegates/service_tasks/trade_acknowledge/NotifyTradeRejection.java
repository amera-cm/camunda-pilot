package io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.service_tasks.trade_acknowledge;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_STATUS_VAR_KEY;

import io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.ActivityDelegate;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.PilotProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component(PilotProcessDefinitionKey.Keys.TRADE_ACKNOWLEDGE_KEY + NotifyTradeRejection.ACTIVITY_ID)
@ParametersAreNonnullByDefault
public class NotifyTradeRejection implements ActivityDelegate {

  public static final String ACTIVITY_ID = "notifyTradeRejection";
  private static final Logger logger = LogManager.getLogger(NotifyTradeRejection.class);

  @Nonnull
  @Override
  public ProcessDefinitionKey processDefinitionKey() {
    return PilotProcessDefinitionKey.TRADE_ACKNOWLEDGE;
  }

  @Nonnull
  @Override
  public String activityId() {
    return ACTIVITY_ID;
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    logger.info(() -> "SEND NOTIFICATION: Trade Rejected");
    delegateExecution.setVariable(
        TRADE_STATUS_VAR_KEY, TradesOps.TradeStatus.TRADE_REJECTED.value());
  }
}
