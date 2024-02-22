package io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.events.trade_nomination;

import io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.ActivityDelegate;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.PilotProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.bpmn.model.ProcessDefinitionKey;
import io.cmt.camunda_pilot.camunda.boot.events.model.TradeCreatedEvent;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component(PilotProcessDefinitionKey.Keys.TRADE_NOMINATION_KEY + TradeCreated.ACTIVITY_ID)
@ParametersAreNonnullByDefault
public class TradeCreated implements ActivityDelegate {

  public static final String ACTIVITY_ID = "tradeCreated";
  private static final Logger logger = LogManager.getLogger(TradeCreated.class);
  private final ApplicationEventPublisher eventPublisher;

  public TradeCreated(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Nonnull
  @Override
  public ProcessDefinitionKey processDefinitionKey() {
    return PilotProcessDefinitionKey.TRADE_NOMINATION;
  }

  @Nonnull
  @Override
  public String activityId() {
    return ACTIVITY_ID;
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    logger.debug(() -> "TradeCreated::execute");
    eventPublisher.publishEvent(new TradeCreatedEvent(delegateExecution.getVariablesTyped()));
  }
}
