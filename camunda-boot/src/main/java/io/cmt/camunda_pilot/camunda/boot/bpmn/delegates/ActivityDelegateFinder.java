package io.cmt.camunda_pilot.camunda.boot.bpmn.delegates;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public final class ActivityDelegateFinder {

  private static final Logger logger = LogManager.getLogger(ActivityDelegateFinder.class);

  private final ApplicationContext applicationContext;

  public ActivityDelegateFinder(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Nonnull
  private static String buildFinderKey(String processDefinitionKey, String activityId) {
    return processDefinitionKey + activityId;
  }

  @Nonnull
  public ActivityDelegate find(String processDefinitionKey, String activityId) {
    final var finderKey = buildFinderKey(processDefinitionKey, activityId);
    logger.debug(() -> "Finding an activity delegate with finder key: " + finderKey);
    try {
      return applicationContext.getBean(finderKey, ActivityDelegate.class);
    } catch (BeansException e) {
      throw new RuntimeException(
          "An error occurred finding an activity delegate with finder key %s".formatted(finderKey),
          e);
    }
  }
}
