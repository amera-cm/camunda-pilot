package io.cmt.camunda_pilot.camunda.boot.events.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TradeCapacityUpdatedEvent {
  String signalName = "tradeCapacityUpdated";
}
