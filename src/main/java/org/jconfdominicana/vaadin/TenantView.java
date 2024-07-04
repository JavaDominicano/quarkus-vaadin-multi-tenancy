package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.repositories.common.TenantRepository;
import org.jconfdominicana.security.vaadin.CacheService;
import org.jconfdominicana.security.vaadin.SecurityService;

import java.util.concurrent.ExecutionException;

@PageTitle("Tenant")
@Route(value = "tenant")
@RouteAlias(value = "")
@PermitAll
public class TenantView extends VerticalLayout implements BeforeEnterObserver {


    private final SecurityService securityService;
    private final TenantRepository tenantRepository;
    private final CacheService cacheService;


    public TenantView(SecurityService securityService, TenantRepository tenantRepository, CacheService cacheService) {
        this.securityService = securityService;
        this.tenantRepository = tenantRepository;
        this.cacheService = cacheService;

        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassNames(LumoUtility.Padding.XLARGE);
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        securityService.getUsername().ifPresent(username -> {
            try {
                Tenant tenant = cacheService.getTenant(username);
                if (tenant != null) {
                    UI.getCurrent().navigate(EmptyView.class);
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        var variants = new ButtonVariant[]{ButtonVariant.LUMO_PRIMARY};
        var width = "200px";
        var height = "100px";


        securityService.getUser().ifPresent(user -> {
            if (user.getTenants().size() == 1) {
                cacheService.putTenant(user.getUsername(), user.getTenants().iterator().next().getTenant());

                event.forwardTo(EmptyView.class);
            } else {

                user.getTenants().forEach(tenant -> {
                    Button component = new Button(tenant.getTenant().getName());
                    component.addThemeVariants(variants);
                    component.setWidth(width);
                    component.setHeight(height);
                    component.addClickListener(e -> {
                        cacheService.putTenant(user.getUsername(), tenant.getTenant());

                        UI.getCurrent().navigate(EmptyView.class);
                    });
                    add(component);
                });
            }
        });


    }
}
