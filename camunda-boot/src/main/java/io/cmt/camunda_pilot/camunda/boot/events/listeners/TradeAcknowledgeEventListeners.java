package io.cmt.camunda_pilot.camunda.boot.events.listeners;

import io.cmt.camunda_pilot.camunda.boot.events.model.RejectTradeEventCmd;
import io.cmt.camunda_pilot.camunda.boot.events.model.TradeAcknowledgedEvent;
import io.cmt.camunda_pilot.camunda.boot.events.model.TradeCapacityUpdatedEvent;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class TradeAcknowledgeEventListeners {

  private static final Logger logger = LogManager.getLogger(TradeAcknowledgeEventListeners.class);

  private final RuntimeService runtimeService;

  public TradeAcknowledgeEventListeners(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  @EventListener
  public void handleTradeCapacityUpdated(TradeCapacityUpdatedEvent event) {
    logger.debug(() -> "TradeAcknowledgeEventListeners::handleTradeCapacityUpdated");
    runtimeService.signalEventReceived(event.getSignalName());
    logger.debug(() -> "Signal event received: " + event.getSignalName());
  }

  @EventListener
  public void handleRejectTradeEventCmd(RejectTradeEventCmd event) {
    logger.debug(() -> "TradeAcknowledgeEventListeners::handleRejectTradeEventCmd");
    runtimeService.correlateMessage(event.getMessageName());
    logger.debug(() -> "Correlate message: " + event.getMessageName());
  }

  @EventListener
  public void handleTradeAcknowledgedEvent(TradeAcknowledgedEvent event) {
    logger.debug(() -> "TradeAcknowledgeEventListeners::handleTradeAcknowledgedEvent");
    logger.debug(() -> "Received Trade acknowledged event with tradeId: " + event.getTradeId());
  }
}
