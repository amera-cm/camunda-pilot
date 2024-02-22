package io.cmt.camunda_pilot.camunda.boot.bpmn.external_tasks.trade_acknowledge;

import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.CAPACITY_AVAILABLE_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.IS_CAPACITY_ENOUGH_VAR_KEY;
import static io.cmt.camunda_pilot.camunda.boot.ops.TradesOps.NOMINATION_AMOUNT_VAR_KEY;

import io.cmt.camunda_pilot.camunda.boot.bpmn.external_tasks.ExternalTaskHandler;
import io.cmt.camunda_pilot.camunda.boot.ops.TradesOps;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class EvaluateTradeCapacityExtTaskHandler implements ExternalTaskHandler {

  public static final String TOPIC_NAME = "tradeAcknowledge_evaluateTradeCapacityTopic";
  private static final Logger logger =
      LogManager.getLogger(EvaluateTradeCapacityExtTaskHandler.class);

  @Nonnull private final TradesOps tradesOps;
  @Nonnull private final ExternalTaskService taskService;

  public EvaluateTradeCapacityExtTaskHandler(TradesOps tradesOps, ExternalTaskService taskService) {
    this.tradesOps = tradesOps;
    this.taskService = taskService;
  }

  @Nonnull
  @Override
  public String topic() {
    return TOPIC_NAME;
  }

  @Override
  public void handle(String workerId, LockedExternalTask task) {
    logger.debug(() -> "EvaluateTradeCapacityExtTaskHandler::handle::" + topic());
    final var model = new HashMap<String, Object>();
    final var capacityAvailable = tradesOps.availableCapacity();
    final var amountRequired =
        task.getVariables().getValue(NOMINATION_AMOUNT_VAR_KEY, Double.class);
    final var capacityEnough = capacityAvailable > amountRequired;

    model.put(CAPACITY_AVAILABLE_VAR_KEY, capacityAvailable);
    model.put(IS_CAPACITY_ENOUGH_VAR_KEY, capacityEnough);

    if (capacityEnough) {
      tradesOps.reserveCapacity(amountRequired);
    }

    taskService.complete(task.getId(), workerId, model);
  }
}
