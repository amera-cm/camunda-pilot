package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms;

import javax.annotation.Nonnull;
import org.springframework.context.ApplicationContext;

public interface FormFactory {
  @Nonnull
  String processDefinitionKey();

  @Nonnull
  String taskDefinitionKey();

  @Nonnull
  UserTaskFormPanel create(Object payload, ApplicationContext applicationContext);
}
