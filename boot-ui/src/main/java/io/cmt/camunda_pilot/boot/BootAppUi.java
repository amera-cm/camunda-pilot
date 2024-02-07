package io.cmt.camunda_pilot.boot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import io.cmt.camunda_pilot.boot.spring.BootUiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Theme(value = "pilot")
@SpringBootApplication(scanBasePackageClasses = {BootUiConfig.class})
public class BootAppUi implements AppShellConfigurator {

  public static void main(String[] args) {
    SpringApplication.run(BootAppUi.class, args);
  }
}
