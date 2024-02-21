package io.cmt.camunda_pilot.camunda.boot.bpmn.listeners;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public final class UserTaskListenerFinder {

  private static final Logger logger = LogManager.getLogger(UserTaskListenerFinder.class);

  private final ApplicationContext applicationContext;

  public UserTaskListenerFinder(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Nonnull
  private static String buildFinderKey(String processDefinitionKey, String taskDefinitionKey) {
    return processDefinitionKey + taskDefinitionKey;
  }

  @Nonnull
  public UserTaskCreateListener findCreateListener(
      String processDefinitionKey, String taskDefinitionKey) {
    final var finderKey = buildFinderKey(processDefinitionKey, taskDefinitionKey);
    logger.debug(() -> "Finding an user task create listener with finder key: " + finderKey);
    try {
      return applicationContext.getBean(finderKey, UserTaskCreateListener.class);
    } catch (BeansException e) {
      throw new RuntimeException(
          "An error occurred finding an user task create listener with finder key %s"
              .formatted(finderKey),
          e);
    }
  }
}
