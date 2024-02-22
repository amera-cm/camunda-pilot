package io.cmt.camunda_pilot.camunda.boot.ops;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.DISCOUNT_AMOUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_ACKNOWLEDGE_STATUS_VAR_KEY;

import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TradeAcknowledgeStatus;
import java.util.HashMap;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.TaskService;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class TradeAcknowledgeUserTasksOps {
  private static final Logger logger = LogManager.getLogger(TradeAcknowledgeUserTasksOps.class);

  private final TaskService taskService;

  public TradeAcknowledgeUserTasksOps(TaskService taskService) {
    this.taskService = taskService;
  }

  public void setupTrade(String taskId, Double discountAmount, UserTaskCmd cmd) {
    logger.debug(() -> "TradeAcknowledgeUserTasksOps::setupTrade");
    final var model = new HashMap<String, Object>();
    model.put(DISCOUNT_AMOUNT_VAR_KEY, discountAmount);
    if (cmd == UserTaskCmd.SAVE) {
      taskService.setVariables(taskId, model);
    } else if (cmd == UserTaskCmd.COMPLETE) {
      taskService.complete(taskId, model);
    }
  }

  public void setTradeAcknowledgeStatus(
      String taskId, TradeAcknowledgeStatus acknowledgeStatus, UserTaskCmd cmd) {
    logger.debug(() -> "TradeAcknowledgeUserTasksOps::setTradeAcknowledgeStatus");
    final var model = new HashMap<String, Object>();
    model.put(TRADE_ACKNOWLEDGE_STATUS_VAR_KEY, acknowledgeStatus.value());
    if (cmd == UserTaskCmd.SAVE) {
      taskService.setVariables(taskId, model);
    } else if (cmd == UserTaskCmd.COMPLETE) {
      taskService.complete(taskId, model);
    }
  }
}
