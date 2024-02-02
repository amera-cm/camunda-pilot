package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLayout;
import javax.annotation.ParametersAreNonnullByDefault;

@ParentLayout(RootAppView.class)
@ParametersAreNonnullByDefault
public class RootContentRouterLayout extends Div implements RouterLayout {}
