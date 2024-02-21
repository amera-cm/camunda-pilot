package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms;

import com.vaadin.flow.component.Component;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.springframework.context.ApplicationContext;

public interface FormFactory {
  @Nonnull
  String processDefinitionKey();

  @Nonnull
  String taskDefinitionKey();

  @Nonnull
  Component create(
      Object payload, ApplicationContext applicationContext, Function<Void, Void> afterCompleteFn);
}
