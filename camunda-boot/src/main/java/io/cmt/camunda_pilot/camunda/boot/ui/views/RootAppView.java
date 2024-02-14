package io.cmt.camunda_pilot.camunda.boot.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.cmt.camunda_pilot.camunda.boot.ui.adapters.SecurityUiAdapter;
import io.cmt.camunda_pilot.camunda.boot.ui.components.MySideNavItem;
import io.cmt.camunda_pilot.camunda.boot.ui.components.RootHeadViewTitle;
import io.cmt.camunda_pilot.camunda.boot.ui.components.UserMenuBar;
import jakarta.annotation.security.PermitAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@PermitAll
@PageTitle("Camunda Pilot")
@Route("")
public class RootAppView extends AppLayout {

  private static final Logger logger = LogManager.getLogger(RootAppView.class);
  private final SecurityUiAdapter securityAdapter;

  public RootAppView(SecurityUiAdapter securityAdapter) {
    this.securityAdapter = securityAdapter;
    H1 appTitle = new H1("Camunda Pilot");
    appTitle
        .getStyle()
        .set("font-size", "var(--lumo-font-size-l)")
        .set("line-height", "var(--lumo-size-l)")
        .set("margin", "0 var(--lumo-space-m)");

    SideNav views = getPrimaryNavigation();

    Scroller scroller = new Scroller(views);
    scroller.setClassName(LumoUtility.Padding.SMALL);

    //    HorizontalLayout subViews = getSecondaryNavigation();
    //    subViews.getElement();

    //    VerticalLayout viewHeader = new VerticalLayout(wrapper, subViews);

    addToDrawer(appTitle, scroller);
    addToNavbar(topNavBar());

    setPrimarySection(Section.DRAWER);

    setContent(new RootContentRouterLayout());
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    logger.info("RootAppView::onAttach");
  }

  private HorizontalLayout topNavBar() {
    final var toggle = new DrawerToggle();
    final var viewTitle = new RootHeadViewTitle("Welcome");
    final var userMenu = new UserMenuBar(securityAdapter);
    final var userMenuWrapper = new FlexLayout(userMenu);
    userMenuWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

    HorizontalLayout hlayout = new HorizontalLayout(toggle, viewTitle, userMenuWrapper);
    hlayout.setAlignItems(FlexComponent.Alignment.CENTER);
    hlayout.setSpacing(false);
    hlayout.expand(userMenuWrapper);
    hlayout.setWidthFull();
    //
    //    VerticalLayout viewHeader = new VerticalLayout(wrapper);
    //    viewHeader.setPadding(false);
    //    viewHeader.setSpacing(false);
    return hlayout;
  }

  private SideNav getPrimaryNavigation() {
    SideNav sideNav = new SideNav();
    sideNav.addItem(
        createSideNavItem("Home", HomeUserView.class, VaadinIcon.HOME.create()),
        createSideNavItem("Dashboard", "/dashboard", VaadinIcon.DASHBOARD.create()),
        createSideNavItem("Orders", "/orders", VaadinIcon.CART.create()),
        createSideNavItem("Customers", "/customers", VaadinIcon.USER_HEART.create()),
        createSideNavItem("Products", "/products", VaadinIcon.PACKAGE.create()),
        createSideNavItem("Documents", "/documents", VaadinIcon.RECORDS.create()),
        createSideNavItem("Tasks", "/tasks", VaadinIcon.LIST.create()),
        createSideNavItem(
            "Camunda Apps", "http://localhost:8080/camunda/app/", VaadinIcon.CHART.create()));
    return sideNav;
  }

  private HorizontalLayout getSecondaryNavigation() {
    HorizontalLayout navigation = new HorizontalLayout();
    navigation.addClassNames(
        LumoUtility.JustifyContent.CENTER, LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM);
    navigation.add(
        createLink("All Users", HomeUserView.class),
        createLink("Super Users", HomeSuperuserView.class));
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
