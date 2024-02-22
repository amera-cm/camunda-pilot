package io.cmt.camunda_pilot.camunda.boot.ops;

import io.cmt.camunda_pilot.camunda.boot.events.model.TradeCapacityUpdatedEvent;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class TradesOps {

  public static final String TRADE_STATUS_VAR_KEY = "tradeStatus";
  public static final String TRADE_ID_VAR_KEY = "tradeId";
  public static final String NOMINATION_AMOUNT_VAR_KEY = "nominationAmount";
  public static final String ASSETS_COUNT_VAR_KEY = "assetsCount";
  public static final String NOMINATION_APPROVAL_STATUS_VAR_KEY = "nominationApprovalStatus";
  public static final String DISCOUNT_AMOUNT_VAR_KEY = "discountAmount";
  public static final String CAPACITY_AVAILABLE_VAR_KEY = "capacityAvailable";
  public static final String IS_CAPACITY_ENOUGH_VAR_KEY = "isCapacityEnough";
  public static final String TRADE_ACKNOWLEDGE_STATUS_VAR_KEY = "tradeAcknowledgeStatus";

  private final ApplicationEventPublisher eventPublisher;

  private final AtomicReference<Double> capacityAvailableRef = new AtomicReference<>(100_000D);

  public TradesOps(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  public Double availableCapacity() {
    return capacityAvailableRef.get();
  }

  public Double reserveCapacity(Double reserveAmount) {
    return capacityAvailableRef.updateAndGet(available -> available - reserveAmount);
  }

  public Double addCapacity(Double capacity) {
    final var ret = capacityAvailableRef.updateAndGet(avail -> avail + capacity);
    eventPublisher.publishEvent(new TradeCapacityUpdatedEvent());
    return ret;
  }

  public enum TradeStatus {
    NOMINATION_REQUESTED,
    NOMINATION_CREATED,
    TRADE_CREATED,
    NOMINATION_REJECTED,
    TRADE_REJECTED;

    public String value() {
      return this.name();
    }
  }

  public enum NominationApprovalStatus {
    APPROVED,
    REJECTED;

    public String value() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }

  public enum TradeAcknowledgeStatus {
    ACKNOWLEDGED,
    REJECTED;

    public static TradeAcknowledgeStatus fromValue(String value) {
      return TradeAcknowledgeStatus.valueOf(value.toUpperCase(Locale.ROOT));
    }

    public String value() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
