package io.cmt.camunda_pilot.camunda.boot.ui.views;

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
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;

@ParentLayout(RootContentRouterLayout.class)
@RoutePrefix("home")
@ParametersAreNonnullByDefault
public class HomeTabsRouterLayout extends VerticalLayout
    implements RouterLayout, BeforeEnterObserver {

  public HomeTabsRouterLayout() {
    final var routeTabs = new RouteTabs();
    routeTabs.add(new RouterLink("All Users", HomeUserView.class));
    routeTabs.add(new RouterLink("Super Users", HomeSuperuserView.class));
    setPadding(false);
    setSpacing(false);
    add(routeTabs);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getNavigationTarget() == HomeTabsRouterLayout.class) {
      event.forwardTo(HomeUserView.class);
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
