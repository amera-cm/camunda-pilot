package io.cmt.camunda_pilot.camunda.boot.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MySideNavItem extends SideNavItem implements AfterNavigationObserver {

  public MySideNavItem(String label, String path, Component prefixComponent) {
    super(label, path, prefixComponent);
  }

  public MySideNavItem(String label, Class<? extends Component> view, Component prefixComponent) {
    super(label, view, prefixComponent);
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    final var newPath = event.getLocation().getPath();
    final var thisPath = getPath();
    var highlight =
        thisPath.isEmpty() && newPath.isEmpty()
            || (!thisPath.isEmpty() && (newPath.equals(thisPath) || newPath.startsWith(thisPath)));

    if (highlight) {
      addClassName("highlight");
    } else {
      removeClassName("highlight");
    }
  }
}
