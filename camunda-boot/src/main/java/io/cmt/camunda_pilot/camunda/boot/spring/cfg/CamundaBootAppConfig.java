package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import io.cmt.camunda_pilot.camunda.boot.bpmn.delegates.ActivityDelegate;
import io.cmt.camunda_pilot.camunda.boot.bpmn.listeners.UserTaskListener;
import io.cmt.camunda_pilot.camunda.boot.events.listeners.TradeNominationEventListeners;
import io.cmt.camunda_pilot.camunda.boot.ops.UsersOps;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.views.RootAppView;
import io.cmt.camunda_pilot.camunda.boot.web.pages.HomePage;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = {
      ActivityDelegate.class,
      UserTaskListener.class,
      TradeNominationEventListeners.class,
      UsersOps.class,
      HomePage.class,
      SecurityUiAdapter.class,
      RootAppView.class
    })
@ParametersAreNonnullByDefault
public class CamundaBootAppConfig {}
