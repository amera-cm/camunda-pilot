package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import io.cmt.camunda_pilot.camunda.boot.web.pages.HomePage;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {HomePage.class})
@ParametersAreNonnullByDefault
public class CamundaBootAppConfig {}
