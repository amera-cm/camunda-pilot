package io.cmt.camunda_pilot.camunda.boot.events.model;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.ASSETS_COUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_AMOUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_APPROVAL_STATUS_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_ID_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_STATUS_VAR_KEY;

import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NominationApprovalStatus;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TradeStatus;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.camunda.bpm.engine.variable.VariableMap;

@Value
@AllArgsConstructor
public class TradeCreatedEvent {
  String messageName = "tradeCreated";
  String tradeId;
  Double nominationAmount;
  int assetsCount;
  NominationApprovalStatus nominationApprovalStatus;
  TradeStatus tradeStatus;

  public TradeCreatedEvent(VariableMap varMap) {
    this(
        varMap.getValue(TRADE_ID_VAR_KEY, String.class),
        varMap.getValue(NOMINATION_AMOUNT_VAR_KEY, Double.class),
        varMap.getValue(ASSETS_COUNT_VAR_KEY, Integer.class),
        NominationApprovalStatus.valueOf(
            varMap
                .getValue(NOMINATION_APPROVAL_STATUS_VAR_KEY, String.class)
                .toUpperCase(Locale.ROOT)),
        TradeStatus.valueOf(
            varMap.getValue(TRADE_STATUS_VAR_KEY, String.class).toUpperCase(Locale.ROOT)));
  }
}
