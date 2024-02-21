package io.cmt.camunda_pilot.camunda.boot.bpmn.model;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public enum PilotProcessDefinitionKey implements ProcessDefinitionKey {
  LOAN_APPROVAL,
  TRADE_NOMINATION;

  @Nonnull
  @Override
  public String key() {
    final var key =
        switch (this) {
          case LOAN_APPROVAL -> Keys.LOAN_APPROVAL_KEY;
          case TRADE_NOMINATION -> Keys.TRADE_NOMINATION_KEY;
        };
    return key;
  }

  public static class Keys {
    public static final String LOAN_APPROVAL_KEY = "loanApproval";
    public static final String TRADE_NOMINATION_KEY = "tradeNomination";
  }
}
