package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLayout;
import javax.annotation.ParametersAreNonnullByDefault;

@ParentLayout(RootContentRouterLayout.class)
@RoutePrefix("dashboard")
@ParametersAreNonnullByDefault
public class DashboardRouterLayout extends Div implements RouterLayout {}
