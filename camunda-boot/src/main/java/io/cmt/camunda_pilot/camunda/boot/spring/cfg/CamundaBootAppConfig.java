package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import io.cmt.camunda_pilot.camunda.boot.listeners.CreateTaskListener;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.views.RootAppView;
import io.cmt.camunda_pilot.camunda.boot.web.pages.HomePage;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = {
      CreateTaskListener.class,
      HomePage.class,
      SecurityUiAdapter.class,
      RootAppView.class
    })
@ParametersAreNonnullByDefault
public class CamundaBootAppConfig {}
