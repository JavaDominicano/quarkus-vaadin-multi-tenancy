package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.jconfdominicana.config.FlywayService;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.security.vaadin.CacheService;
import org.jconfdominicana.security.vaadin.SecurityService;
import org.jconfdominicana.service.ProfileService;
import org.jconfdominicana.service.TenantService;
import org.jconfdominicana.service.UserService;
import org.jconfdominicana.utlis.NotificationUtils;
import org.jconfdominicana.vaadin.person.PersonView;

import java.util.function.Consumer;

@PageTitle("Tenant")
@Route(value = "tenant", layout = TenantLayout.class)
@RouteAlias(value = "", layout = TenantLayout.class)
//@AnonymousAllowed
@PermitAll
public class TenantView extends VerticalLayout implements BeforeEnterObserver, TenantViewObserver {

    private final transient SecurityService securityService;
    private final transient CacheService cacheService;
    private transient Consumer<Boolean> listLayout;

    private transient TenantForm form;


    public TenantView(SecurityService securityService,  TenantService tenantService, ProfileService profileService,
                      UserService userService, FlywayService flywayService, CacheService cacheService, NotificationUtils notification) {
        this.securityService = securityService;
        this.cacheService = cacheService;

        securityService.getUsername().flatMap(userService::findByUsername).ifPresent(user -> form = new TenantForm(tenantService, profileService, flywayService, cacheService, notification, user));


        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassNames(LumoUtility.Padding.XLARGE);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.getUsername().ifPresent(username -> {
            Tenant tenant = cacheService.getTenant(username);
            if (tenant != null) {
                UI.getCurrent().navigate(PersonView.class);
            }
        });

        securityService.getUser().ifPresent(user -> {
            if (user.getTenants().size() == 1) {
                cacheService.putTenant(user.getUsername(), user.getTenants().iterator().next().getTenant());

                event.forwardTo(PersonView.class);
            } else {
                setList(user);
                listLayout = aBoolean -> {
                    removeAll();
                    if (aBoolean) {
                        setList(user);
                    } else {
                        setGrid(user);
                    }
                };
            }
        });
    }

    private void setList(User user) {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassNames(LumoUtility.AlignItems.CENTER);
        layout.setMaxWidth("1024px");

        add(layout);

        user.getTenants().forEach(t -> {

            Tenant tenant = t.getTenant();

            HorizontalLayout tenantLayout = new HorizontalLayout();
            tenantLayout.addClassName("list-mode");
            tenantLayout.addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.BorderRadius.SMALL, LumoUtility.AlignItems.CENTER);
            tenantLayout.addClassNames(LumoUtility.Padding.XLARGE);
            tenantLayout.setHeight("200px");
            tenantLayout.getStyle().set("cursor", "pointer");
            tenantLayout.addSingleClickListener(event1 -> {
                cacheService.putTenant(user.getUsername(), tenant);

                UI.getCurrent().navigate(PersonView.class);
            });

            Image img = new Image(tenant.getLogo(), tenant.getName());
            img.setWidth("100px");

            VerticalLayout descriptionLayout = new VerticalLayout();
            descriptionLayout.addClassName("description-list");
            H4 name = new H4(tenant.getName());
            H6 slogan = new H6(tenant.getSlogan());
            H5 type = new H5(tenant.getType());

            descriptionLayout.add(name, type, slogan);

            tenantLayout.add(img, descriptionLayout);

            layout.add(tenantLayout);
        });
    }

    private void setGrid(User user) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassNames(LumoUtility.AlignItems.END, LumoUtility.FlexWrap.WRAP, LumoUtility.JustifyContent.CENTER);
        layout.setMaxWidth("1024px");

        add(layout);

        user.getTenants().forEach(t -> {

            Tenant tenant = t.getTenant();

            VerticalLayout tenantLayout = new VerticalLayout();
            tenantLayout.addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.BorderRadius.SMALL, LumoUtility.AlignItems.CENTER);
            tenantLayout.setMaxWidth("400px");
            tenantLayout.setHeight("230px");
            tenantLayout.getStyle().set("cursor", "pointer");
            tenantLayout.addSingleClickListener(event1 -> {
                cacheService.putTenant(user.getUsername(), tenant);

                UI.getCurrent().navigate(PersonView.class);
            });

            Image img = new Image(tenant.getLogo(), tenant.getName());
            img.setWidth("100px");
            tenantLayout.add(img);

            H4 name = new H4(tenant.getName());
            H6 slogan = new H6(tenant.getSlogan());
            H5 type = new H5(tenant.getType());


            tenantLayout.add(name, type, slogan);


            layout.add(tenantLayout);
        });
    }


    @Override
    public void addTenant() {
        if (form == null) return;
        form.createDialog(new Runnable() {
            @Override
            public void run() {
                System.out.println("VAMOS ALLA");
            }
        });
    }

    @Override
    public void listView() {
        listLayout.accept(true);
    }

    @Override
    public void gridView() {
        listLayout.accept(false);
    }
}
