package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.ActivityDelegate;
import io.cmt.camunda_pilot.camunda.boot.bpmn.external_tasks.ExternalTaskHandler;
import io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.UserTaskListener;
import io.cmt.camunda_pilot.camunda.boot.events.listeners.TradeNominationEventListeners;
import io.cmt.camunda_pilot.camunda.boot.ops.UsersOps;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.views.RootAppView;
import io.cmt.camunda_pilot.camunda.boot.web.pages.HomePage;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(
    basePackageClasses = {
      ActivityDelegate.class,
      UserTaskListener.class,
      ExternalTaskHandler.class,
      TradeNominationEventListeners.class,
      UsersOps.class,
      HomePage.class,
      SecurityUiAdapter.class,
      RootAppView.class
    })
@ParametersAreNonnullByDefault
public class CamundaBootAppConfig {

  /** Create Bean TaskExecutor. */
  @Bean
  public TaskExecutor externalTasksWorkerExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("ExtTasksWorker-");
    executor.initialize();
    return executor;
  }
}
