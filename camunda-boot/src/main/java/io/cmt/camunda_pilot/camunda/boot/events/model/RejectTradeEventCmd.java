package io.cmt.camunda_pilot.camunda.boot.events.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class RejectTradeEventCmd {
  String messageName = "rejectTradeCommand";
}
