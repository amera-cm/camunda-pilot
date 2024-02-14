package io.cmt.camunda_pilot.camunda.boot.ui.components;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserMenuBar extends MenuBar {

  private static final Logger logger = LogManager.getLogger(UserMenuBar.class);

  private final SecurityUiAdapter securityAdapter;

  private final MenuItem userNameItem;
  private final Text userNameLabel;

  public UserMenuBar(SecurityUiAdapter securityAdapter) {
    super();
    getStyle().set("marginRight", "var(--lumo-space-m)");
    this.securityAdapter = securityAdapter;
    addThemeVariants(MenuBarVariant.LUMO_ICON);
    this.userNameLabel = new Text("Unknown");
    this.userNameItem = createUserNameItem(this.userNameLabel);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("UserMenuBar onAttach");
    final var user = securityAdapter.getUser();
    userNameLabel.setText(user.getFullName());

    final var ui = attachEvent.getUI();
    final var subMenu = userNameItem.getSubMenu();
    createSubMenuItem(
        subMenu, VaadinIcon.EXIT_O, new Text("Logout"), e -> securityAdapter.logout(ui));
  }

  private MenuItem createUserNameItem(Text label) {
    final var item = this.addItem(new Icon(VaadinIcon.USER), e -> {});
    item.add(label);
    return item;
  }

  private MenuItem createSubMenuItem(
      SubMenu subMenu,
      VaadinIcon iconName,
      Text label,
      @Nullable ComponentEventListener<ClickEvent<MenuItem>> listener) {
    final var icon = new Icon(iconName);
    icon.getStyle().set("width", "var(--lumo-icon-size-s)");
    icon.getStyle().set("height", "var(--lumo-icon-size-s)");
    icon.getStyle().set("marginRight", "var(--lumo-space-s)");
    final var item = subMenu.addItem(icon, e -> {});
    item.add(label);
    if (listener != null) {
      item.addClickListener(listener);
    }
    return item;
  }
}
