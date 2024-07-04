package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.security.vaadin.CacheService;
import org.jconfdominicana.security.vaadin.SecurityService;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private H1 viewTitle;

    private final SecurityService securityService;
    private final CacheService cacheService;
    private final AccessAnnotationChecker accessChecker;

    public MainLayout(SecurityService securityService, AccessAnnotationChecker accessChecker, CacheService cacheService) {
        this.securityService = securityService;
        this.accessChecker = accessChecker;
        this.cacheService = cacheService;

//        setPrimarySection(Section.DRAWER);
//        addDrawerContent();
//        addHeaderContent();
        addToNavbar(createHeaderContent());
        setDrawerOpened(false);
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

//        addToNavbar(true, toggle, viewTitle);

        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();

        securityService.getUsername().ifPresent(username -> {
            try {
                Tenant tenant = cacheService.getTenant(username);
                if (tenant != null) {
                    header.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Width.FULL);

                    Div layout = new Div();
                    layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.LARGE);

                    H1 appName = new H1("JConf - 2024");
                    appName.addClassNames(LumoUtility.Margin.Vertical.MEDIUM, LumoUtility.Margin.End.AUTO, LumoUtility.FontSize.LARGE);
                    layout.add(appName);

                    Optional<Profile> maybeUser = securityService.getProfile();
                    if (maybeUser.isPresent()) {
                        Profile user = maybeUser.get();

                        Avatar avatar = new Avatar(user.getName());
                        StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(new byte[]{}));
                        avatar.setImageResource(resource);
                        avatar.setThemeName("xsmall");
                        avatar.getElement().setAttribute("tabindex", "-1");

                        MenuBar userMenu = new MenuBar();
                        userMenu.setThemeName("tertiary-inline contrast");

                        MenuItem userName = userMenu.addItem("");

                        securityService.userHasSomeTenant().ifPresent(some -> {
                            if (some) {
                                userName.getSubMenu().addItem("Change company", e -> {
                                    securityService.clearSession();
                                    UI.getCurrent().navigate("");
                                });
                            }
                        });

                        Div div = new Div();
                        div.add(avatar);
                        div.add(user.getName());
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

                    Nav nav = new Nav();
                    nav.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Overflow.AUTO, LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Vertical.XSMALL);

                    // Wrap the links in a list; improves accessibility
                    UnorderedList list = new UnorderedList();
                    list.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
                    nav.add(list);

                    for (MenuItemInfo menuItem : createMenuItems()) {
                        if (accessChecker.hasAccess(menuItem.getView())) {
                            list.add(menuItem);
                        }

                    }

                    header.add(layout, nav);

                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });


        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //

                new MenuItemInfo("Empty", LineAwesomeIcon.USER.create(), EmptyView.class), //

        };
    }

    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.XSMALL, LumoUtility.Height.MEDIUM, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.SMALL, LumoUtility.TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(LumoUtility.FontWeight.MEDIUM, LumoUtility.FontSize.MEDIUM, LumoUtility.Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

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
            StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(new byte[]{}));
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
        securityService.getUsername().ifPresent(username -> {
            try {
                Tenant tenant = cacheService.getTenant(username);
                if (tenant == null) {
                    event.forwardTo("");
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    @Override
//    protected void afterNavigation() {
//        super.afterNavigation();
//      //  viewTitle.setText(getCurrentPageTitle());
//    }
//
//    private String getCurrentPageTitle() {
//        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
//        return title == null ? "" : title.value();
//    }
}
