package io.cmt.camunda_pilot.camunda.boot.events.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.camunda.bpm.engine.variable.VariableMap;

@Value
@AllArgsConstructor
public class TradeAcknowledgedEvent {
  String messageName = "tradeAcknowledged";
  String tradeId;
  VariableMap variableMap;
}
