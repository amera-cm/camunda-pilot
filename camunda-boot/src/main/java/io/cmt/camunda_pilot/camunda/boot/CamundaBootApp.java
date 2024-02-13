package io.cmt.camunda_pilot.camunda.boot;

import io.cmt.camunda_pilot.camunda.boot.spring.cfg.CamundaBootAppConfig;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProcessApplication("camundaPilotProcApp")
@SpringBootApplication(scanBasePackageClasses = {CamundaBootAppConfig.class})
public class CamundaBootApp {

  public static void main(String[] args) {
    SpringApplication.run(CamundaBootApp.class, args);
  }
}
