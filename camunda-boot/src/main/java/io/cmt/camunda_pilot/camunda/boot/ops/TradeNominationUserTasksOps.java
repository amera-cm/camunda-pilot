package io.cmt.camunda_pilot.camunda.boot.ops;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.ASSETS_COUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_AMOUNT_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_APPROVAL_STATUS_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.TRADE_STATUS_VAR_KEY;

import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NominationApprovalStatus;
import java.util.HashMap;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.TaskService;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class TradeNominationUserTasksOps {
  private static final Logger logger = LogManager.getLogger(TradeNominationUserTasksOps.class);

  private final TaskService taskService;

  public TradeNominationUserTasksOps(TaskService taskService) {
    this.taskService = taskService;
  }

  public void setupNomination(
      String taskId, Double nominationAmount, int assetsCount, UserTaskCmd cmd) {
    //    final var task = taskService.createTaskQuery().taskId(taskId).singleResult();
    logger.debug(() -> "TradeNominationUserTasksOps::setupNomination");
    final var model = new HashMap<String, Object>();
    model.put(NOMINATION_AMOUNT_VAR_KEY, nominationAmount);
    model.put(ASSETS_COUNT_VAR_KEY, assetsCount);
    if (cmd == UserTaskCmd.SAVE) {
      taskService.setVariables(taskId, model);
    } else if (cmd == UserTaskCmd.COMPLETE) {
      model.put(TRADE_STATUS_VAR_KEY, TradesOps.TradeStatus.NOMINATION_CREATED.value());
      taskService.complete(taskId, model);
    }
  }

  public void setNominationApprovalStatus(
      String taskId, NominationApprovalStatus approvalStatus, UserTaskCmd cmd) {
    logger.debug(() -> "TradeNominationUserTasksOps::setNominationApprovalStatus");
    final var model = new HashMap<String, Object>();
    model.put(NOMINATION_APPROVAL_STATUS_VAR_KEY, approvalStatus.value());
    if (cmd == UserTaskCmd.SAVE) {
      taskService.setVariables(taskId, model);
    } else if (cmd == UserTaskCmd.COMPLETE) {
      taskService.complete(taskId, model);
    }
  }
}
