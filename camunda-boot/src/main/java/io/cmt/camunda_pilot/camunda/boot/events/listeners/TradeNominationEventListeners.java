package io.cmt.camunda_pilot.camunda.boot.events.listeners;

import io.cmt.camunda_pilot.camunda.boot.events.model.TradeCreatedEvent;
import io.cmt.camunda_pilot.camunda.boot.events.model.TradeRequestedEvent;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class TradeNominationEventListeners {

  private static final Logger logger = LogManager.getLogger(TradeNominationEventListeners.class);

  private final RuntimeService runtimeService;
  private final IdGenerator idGenerator;

  public TradeNominationEventListeners(RuntimeService runtimeService, IdGenerator idGenerator) {
    this.runtimeService = runtimeService;
    this.idGenerator = idGenerator;
  }

  @EventListener
  public void handleTradeRequestedEvent(TradeRequestedEvent event) {
    logger.debug(() -> "TradeNominationEventListeners::handleTradeRequestedEvent");
    final var tradeId = idGenerator.getNextId();
    final var variables = Map.<String, Object>of(TradesOps.TRADE_ID_VAR_KEY, tradeId);
    final var instance =
        runtimeService
            .createMessageCorrelation(event.getMessageName())
            .processInstanceBusinessKey(tradeId)
            .setVariables(variables)
            .startMessageOnly()
            .correlateStartMessage();
    logger.debug(() -> "Process instance created: " + instance.getId());
  }

  @EventListener
  public void handleTradeCreatedEvent(TradeCreatedEvent event) {
    logger.debug(() -> "TradeNominationEventListeners::handleTradeCreatedEvent");
    logger.debug(() -> "Received trade created event with tradeId: " + event.getTradeId());
    final var variables =
        Map.<String, Object>of(
            TradesOps.TRADE_STATUS_VAR_KEY, event.getTradeStatus().value(),
            TradesOps.TRADE_ID_VAR_KEY, event.getTradeId(),
            TradesOps.NOMINATION_AMOUNT_VAR_KEY, event.getNominationAmount(),
            TradesOps.ASSETS_COUNT_VAR_KEY, event.getAssetsCount(),
            TradesOps.NOMINATION_APPROVAL_STATUS_VAR_KEY,
                event.getNominationApprovalStatus().value());
    final var instance =
        runtimeService
            .createMessageCorrelation(event.getMessageName())
            .processInstanceBusinessKey(event.getTradeId())
            .setVariables(variables)
            .startMessageOnly()
            .correlateStartMessage();
    logger.debug(() -> "Process instance created: " + instance.getId());
  }
}
