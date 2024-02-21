package io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms;

import io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.trade_nomination.SetNominationApprovalStatusFormFactory;
import io.cmt.camunda_pilot.camunda.boot.ui.views.tasks.forms.trade_nomination.SetupNominationFormFactory;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class FormFactoryFinder {

  private final Map<String, FormFactory> factories;

  public FormFactoryFinder() {
    this.factories = new HashMap<>();
    initFactories();
  }

  private static String mapKey(String processDefinitionKey, String taskDefinitionKey) {
    return processDefinitionKey + "_" + taskDefinitionKey;
  }

  private static String mapKey(FormFactory formFactory) {
    return mapKey(formFactory.processDefinitionKey(), formFactory.taskDefinitionKey());
  }

  public FormFactory find(String processDefinitionKey, String taskDefinitionKey) {
    return factories.get(mapKey(processDefinitionKey, taskDefinitionKey));
  }

  private void initFactories() {
    register(new SetupNominationFormFactory());
    register(new SetNominationApprovalStatusFormFactory());
  }

  private void register(FormFactory formFactory) {
    factories.put(mapKey(formFactory), formFactory);
  }
}
