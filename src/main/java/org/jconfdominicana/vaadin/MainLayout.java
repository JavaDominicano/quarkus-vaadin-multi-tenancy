package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jconfdominicana.config.TenantContext;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.security.vaadin.SecurityService;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private H1 viewTitle;

    private final SecurityService securityService;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(SecurityService securityService, AccessAnnotationChecker accessChecker) {
        this.securityService = securityService;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("My App");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        if (accessChecker.hasAccess(EmptyView.class)) {
            nav.addItem(new SideNavItem("Empty", EmptyView.class, LineAwesomeIcon.FILE.create()));
        }

        if (accessChecker.hasAccess(Empty2View.class)) {
            nav.addItem(new SideNavItem("Empty 2", Empty2View.class, LineAwesomeIcon.FILE.create()));
        }
//        if (accessChecker.hasAccess(MasterDetailView.class)) {
//            nav.addItem(
//                    new SideNavItem("Master-Detail", MasterDetailView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
//
//        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<Profile> maybeUser = securityService.getProfile();
        if (maybeUser.isPresent()) {
            Profile profile = maybeUser.get();

            Avatar avatar = new Avatar(profile.getUsername());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(new byte[]{}));
            avatar.setImageResource(resource);
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(profile.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> securityService.logout());

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (TenantContext.getCurrentTenant() == null || TenantContext.getCurrentTenant().isEmpty()) {
            event.forwardTo("");
        }
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
