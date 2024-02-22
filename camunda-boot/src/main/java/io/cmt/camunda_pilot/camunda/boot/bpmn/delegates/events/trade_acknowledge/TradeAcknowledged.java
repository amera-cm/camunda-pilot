package io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.events.trade_acknowledge;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_ID_VAR_KEY;

import io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.ActivityDelegate;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.PilotProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.events.model.TradeAcknowledgedEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component(PilotProcessDefinitionKey.Keys.TRADE_ACKNOWLEDGE_KEY + TradeAcknowledged.ACTIVITY_ID)
@ParametersAreNonnullByDefault
public class TradeAcknowledged implements ActivityDelegate {

  public static final String ACTIVITY_ID = "tradeAcknowledged";
  private static final Logger logger = LogManager.getLogger(TradeAcknowledged.class);
  private final ApplicationEventPublisher eventPublisher;

  public TradeAcknowledged(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

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
    logger.debug(() -> "TradeAcknowledged::execute");
    final StringValue tradeId = delegateExecution.getVariableTyped(TRADE_ID_VAR_KEY);
    eventPublisher.publishEvent(
        new TradeAcknowledgedEvent(tradeId.getValue(), delegateExecution.getVariablesTyped()));
  }
}
