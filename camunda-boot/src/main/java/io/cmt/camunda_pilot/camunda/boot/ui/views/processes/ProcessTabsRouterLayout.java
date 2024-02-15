package io.cmt.camunda_pilot.camunda.boot.ui.views.processes;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RoutePrefix;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import io.cmt.camunda_pilot.camunda.boot.ui.views.RootContentRouterLayout;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;

@ParentLayout(RootContentRouterLayout.class)
@RoutePrefix("process")
@ParametersAreNonnullByDefault
public class ProcessTabsRouterLayout extends VerticalLayout
    implements RouterLayout, BeforeEnterObserver {

  public ProcessTabsRouterLayout() {
    final var routeTabs = new RouteTabs();
    routeTabs.add(new RouterLink("Instances", ProcessInstancesView.class));
    routeTabs.add(new RouterLink("Definitions", ProcessDefinitionsView.class));
    setPadding(false);
    setSpacing(false);
    add(routeTabs);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getNavigationTarget() == ProcessTabsRouterLayout.class) {
      event.forwardTo(ProcessInstancesView.class);
    }
  }

  private static class RouteTabs extends Tabs implements BeforeEnterObserver {
    private final Map<RouterLink, Tab> routerLinkTabMap = new HashMap<>();

    public void add(RouterLink routerLink) {
      routerLink.setHighlightCondition(HighlightConditions.sameLocation());
      routerLink.setHighlightAction(
          (link, shouldHighlight) -> {
            if (shouldHighlight) setSelectedTab(routerLinkTabMap.get(routerLink));
          });
      routerLinkTabMap.put(routerLink, new Tab(routerLink));
      add(routerLinkTabMap.get(routerLink));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
      // In case no tabs will match
      setSelectedTab(null);
    }
  }
}
