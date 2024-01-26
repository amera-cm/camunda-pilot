package io.cmt.camunda_pilot.spring;

import io.cmt.camunda_pilot.web.api.Api;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/** Camunda Pilot Config. */
@Configuration
@ComponentScan(basePackageClasses = {Api.class})
@ParametersAreNonnullByDefault
public class CamundaPilotConfig {}
