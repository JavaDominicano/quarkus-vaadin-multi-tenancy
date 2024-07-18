package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jconfdominicana.config.FlywayService;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.model.common.TenantUser;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.security.vaadin.CacheService;
import org.jconfdominicana.security.vaadin.SecurityService;
import org.jconfdominicana.service.ProfileService;
import org.jconfdominicana.service.TenantService;
import org.jconfdominicana.utlis.NotificationUtils;
import org.jconfdominicana.vaadin.person.PersonView;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private H1 viewTitle;

    private final Select<Tenant> select = new Select<>();

    private final TenantForm form;
    private final SecurityService securityService;
    private final CacheService cacheService;
    private final AccessAnnotationChecker accessChecker;
    private final ProfileService profileService;

    public MainLayout(SecurityService securityService, AccessAnnotationChecker accessChecker, CacheService cacheService,
                      TenantService tenantService, ProfileService profileService,
                      FlywayService flywayService, NotificationUtils notification) {
        this.securityService = securityService;
        this.accessChecker = accessChecker;
        this.cacheService = cacheService;
        this.profileService = profileService;


        form = new TenantForm(tenantService, profileService, flywayService, cacheService, notification);

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
        select.setId("select-tenant");

        select.addClassNames(LumoUtility.Width.FULL, LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE);
        select.addClassNames(LumoUtility.BorderColor.PRIMARY);
        select.getStyle()
                .set("border-width", "2px")
                .set("--vaadin-input-field-background", "transparent");

        select.setRenderer(new ComponentRenderer<>(tenant -> {
            FlexLayout wrapper = new FlexLayout();
            wrapper.setAlignItems(Alignment.CENTER);

            Image image = new Image();
            if (tenant.getLogo() != null) {
                image.setSrc(tenant.getLogo());
                image.setAlt(tenant.getName());
                image.addClassNames(LumoUtility.Width.MEDIUM, LumoUtility.Margin.Right.MEDIUM);
                wrapper.add(image);
            }

            Div info = new Div();
            info.setText(tenant.getName());

            Div details = new Div();
            details.setText(tenant.getSlogan());
            details.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
            info.add(details);

            wrapper.add(info);
            return wrapper;
        }));

        Header header = new Header(select);
        header.addClassNames(LumoUtility.Margin.MEDIUM);

        Scroller scroller = new Scroller(createNavigation());
        scroller.addClassNames(LumoUtility.Margin.MEDIUM, LumoUtility.Flex.AUTO);

        Footer footer = createFooter();
        footer.addClassNames(LumoUtility.Margin.MEDIUM);

        Div layout = new Div(header, scroller, footer);
        layout.addClassNames(LumoUtility.Height.FULL, LumoUtility.Padding.MEDIUM);
        layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);

        addToDrawer(layout);
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Persons", PersonView.class, LineAwesomeIcon.USER.create()));

        return nav;
    }

    private void reloadSelect(Tenant tenant) {
        List<Tenant> tenantUsers = new ArrayList<>(securityService.getUser().map(User::getTenants).orElse(new HashSet<>()).stream().map(TenantUser::getTenant).toList());
        tenantUsers.add(getTenant());
        select.setItems(tenantUsers);
        select.setValue(tenant);
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        securityService.getUsername().ifPresent(username -> {
            Tenant tenant = cacheService.getTenant(username);
            if (tenant != null) {

                reloadSelect(tenant);

                select.addValueChangeListener(event -> {

                    if (event.getValue() == null) return;

                    if (event.getValue().getTenantId().equals("NEW")) {

                        securityService.getUser().ifPresent(form::setUser);

                        form.createDialog(() -> reloadSelect(tenant));
                    } else {
                        cacheService.putTenant(username, event.getValue());
                        UI.getCurrent().getPage().reload();
                    }
                });

                Optional<Profile> optional = profileService.findByUsername(username);

                if (optional.isPresent()) {
                    Profile profile = optional.get();

                    cacheService.putProfile(username, profile);

                    Avatar avatar = new Avatar(profile.getName());
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
                    div.add(profile.getName());
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
            }
        });

        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.getUsername().ifPresent(username -> {
            Tenant tenant = cacheService.getTenant(username);
            if (tenant == null) {
                event.forwardTo("");
            }
        });
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

    private Tenant getTenant() {

        Tenant tenant = new Tenant();
        tenant.setTenantId("NEW");
        tenant.setName("Add");
        tenant.setSlogan("Add new tenant");
        tenant.setLogo("images/add.png");


        return tenant;
    }
}
