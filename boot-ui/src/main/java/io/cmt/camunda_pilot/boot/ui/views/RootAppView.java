package io.cmt.camunda_pilot.boot.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.cmt.camunda_pilot.boot.ui.components.MySideNavItem;
import io.cmt.camunda_pilot.boot.ui.components.RootHeadViewTitle;

@PageTitle("Camunda Pilot")
@Route("")
public class RootAppView extends AppLayout {

  public RootAppView() {
    H1 appTitle = new H1("Camunda Pilot");
    appTitle
        .getStyle()
        .set("font-size", "var(--lumo-font-size-l)")
        .set("line-height", "var(--lumo-size-l)")
        .set("margin", "0 var(--lumo-space-m)");

    SideNav views = getPrimaryNavigation();

    Scroller scroller = new Scroller(views);
    scroller.setClassName(LumoUtility.Padding.SMALL);

    DrawerToggle toggle = new DrawerToggle();

    H2 viewTitle = new RootHeadViewTitle("Home");

    HorizontalLayout subViews = getSecondaryNavigation();
    subViews.getElement();

    HorizontalLayout wrapper = new HorizontalLayout(toggle, viewTitle);
    wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
    wrapper.setSpacing(false);

    VerticalLayout viewHeader = new VerticalLayout(wrapper, subViews);
    viewHeader.setPadding(false);
    viewHeader.setSpacing(false);

    addToDrawer(appTitle, scroller);
    addToNavbar(viewHeader);

    setPrimarySection(Section.DRAWER);

    setContent(new RootContentRouterLayout());
  }

  private SideNav getPrimaryNavigation() {
    SideNav sideNav = new SideNav();
    sideNav.addItem(
        createSideNavItem("Home", "/", VaadinIcon.HOME.create()),
        createSideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()),
        createSideNavItem("Orders", "/orders", VaadinIcon.CART.create()),
        createSideNavItem("Customers", "/customers", VaadinIcon.USER_HEART.create()),
        createSideNavItem("Products", "/products", VaadinIcon.PACKAGE.create()),
        createSideNavItem("Documents", "/documents", VaadinIcon.RECORDS.create()),
        createSideNavItem("Tasks", "/tasks", VaadinIcon.LIST.create()),
        createSideNavItem("Analytics", "/analytics", VaadinIcon.CHART.create()));
    return sideNav;
  }

  private HorizontalLayout getSecondaryNavigation() {
    HorizontalLayout navigation = new HorizontalLayout();
    navigation.addClassNames(
        LumoUtility.JustifyContent.CENTER, LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM);
    navigation.add(
        createLink("All Users", DashboardView.class),
        createLink("Super Users", DashboardSuperUsersView.class));
    return navigation;
  }

  private MySideNavItem createSideNavItem(String label, String path, Component prefixComp) {
    final var item = new MySideNavItem(label, path, prefixComp);
    return item;
  }

  private MySideNavItem createSideNavItem(
      String label, Class<? extends Component> view, Component prefixComp) {
    final var item = new MySideNavItem(label, view, prefixComp);
    return item;
  }

  private RouterLink createLink(String text, Class<? extends Component> targetView) {
    RouterLink link = new RouterLink(text, targetView);

    link.addClassNames(
        LumoUtility.Display.FLEX,
        LumoUtility.AlignItems.CENTER,
        LumoUtility.Padding.Horizontal.MEDIUM,
        LumoUtility.TextColor.SECONDARY,
        LumoUtility.FontWeight.MEDIUM);
    link.addClassName("secondaryNavLink");
    link.getStyle().set("text-decoration", "none");
    link.setHighlightCondition(HighlightConditions.sameLocation());

    return link;
  }
}
