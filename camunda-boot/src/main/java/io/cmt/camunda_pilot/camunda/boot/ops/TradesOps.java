package io.cmt.camunda_pilot.camunda.boot.ops;

import java.util.Locale;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class TradesOps {

  public static final String TRADE_STATUS_VAR_KEY = "tradeStatus";
  public static final String TRADE_ID_VAR_KEY = "tradeId";
  public static final String NOMINATION_AMOUNT_VAR_KEY = "nominationAmount";
  public static final String ASSETS_COUNT_VAR_KEY = "assetsCount";
  public static final String NOMINATION_APPROVAL_STATUS_VAR_KEY = "nominationApprovalStatus";

  public enum TradeStatus {
    NOMINATION_REQUESTED,
    NOMINATION_CREATED,
    TRADE_CREATED,
    NOMINATION_REJECTED;

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
}
