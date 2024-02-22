package io.cmt.camunda_pilot.camunda.boot.bpmn.external_tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
@ParametersAreNonnullByDefault
public class ExternalTasksWorker {

  private static final Logger logger = LogManager.getLogger(ExternalTasksWorker.class);

  private static final String WORKER_ID = "pilotExternalTasksWorker";

  @Nonnull private final ExternalTaskService taskService;
  @Nonnull private final ListableBeanFactory beanFactory;
  @Nonnull private final TaskExecutor taskExecutor;

  private boolean working = false;

  public ExternalTasksWorker(
      ExternalTaskService taskService,
      ListableBeanFactory beanFactory,
      @Qualifier("externalTasksWorkerExecutor") TaskExecutor taskExecutor) {
    this.taskService = taskService;
    this.beanFactory = beanFactory;
    this.taskExecutor = taskExecutor;
  }

  private Future<Void> work() {
    final var future = new CompletableFuture<Void>();
    taskExecutor.execute(
        () -> {
          logger.debug(() -> "Fetching external tasks");
          final var topicMapper = createTopicBeanNameMapper();
          final var builder = taskQueryBuilder(topicMapper.keySet());
          do {
            final var tasks = builder.execute();
            for (LockedExternalTask task : tasks) {
              final var handler =
                  beanFactory.getBean(
                      topicMapper.get(task.getTopicName()), ExternalTaskHandler.class);
              handler.handle(WORKER_ID, task);
            }
          } while (working);
          logger.debug(() -> "Fetching external tasks stopped");
          future.complete(null);
        });
    return future;
  }

  @Nonnull
  private ExternalTaskQueryBuilder taskQueryBuilder(Set<String> topics) {
    final var builder = taskService.fetchAndLock(10, WORKER_ID);
    for (String topic : topics) {
      builder.topic(topic, 60L * 1000L);
    }
    return builder;
  }

  @Nonnull
  private Map<String, String> createTopicBeanNameMapper() {
    final var mapper = new HashMap<String, String>();
    final var beanNames = beanFactory.getBeanNamesForType(ExternalTaskHandler.class);
    for (String beanName : beanNames) {
      final var handler = beanFactory.getBean(beanName, ExternalTaskHandler.class);
      mapper.put(handler.topic(), beanName);
    }
    return mapper;
  }

  @EventListener(ContextRefreshedEvent.class)
  public void start() {
    if (!working) {
      working = true;
      work();
    } else {
      logger.warn("Fetching external tasks was already started");
    }
  }

  public void stop() {
    if (working) {
      working = false;
    } else {
      logger.warn("Fetching external tasks isn't started");
    }
  }

  public boolean isWorking() {
    return working;
  }
}
