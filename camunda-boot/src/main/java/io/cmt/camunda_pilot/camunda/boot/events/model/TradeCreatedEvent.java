package io.cmt.camunda_pilot.camunda.boot.events.model;

import lombok.Value;

@Value
public class TradeCreatedEvent {
  String messageName = "tradeCreated";
  String tradeId;
}
