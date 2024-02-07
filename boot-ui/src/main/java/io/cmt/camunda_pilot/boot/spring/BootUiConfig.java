package io.cmt.camunda_pilot.boot.spring;

import io.cmt.camunda_pilot.boot.adapters.SecurityAdapter;
import io.cmt.camunda_pilot.boot.ui.views.RootAppView;
import io.cmt.camunda_pilot.boot.web.pages.LogoutPage;
import io.cmt.camunda_pilot.boot.web.rest.Api;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = {SecurityAdapter.class, RootAppView.class, Api.class, LogoutPage.class})
public class BootUiConfig {}
