package io.cmt.camunda_pilot;

import io.cmt.camunda_pilot.spring.CamundaPilotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot App. */
@SpringBootApplication(scanBasePackageClasses = {CamundaPilotConfig.class})
public class App {
  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  public String getGreeting() {
    return "Hello World!";
  }
}
