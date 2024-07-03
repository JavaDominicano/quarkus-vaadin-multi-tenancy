package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.jconfdominicana.model.common.Tenant;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@PageTitle("Tenant")
@Route(value = "tenant")
@RouteAlias(value = "")
@PermitAll
public class TenantView extends VerticalLayout implements BeforeEnterObserver {


    @Inject
    DataSource dataSource;

//    private final AuthenticationContext authenticationContext;
//    private final TenantService tenantService;


    public TenantView(/*AuthenticationContext authenticationContext, TenantService tenantService*/) {
//        this.authenticationContext = authenticationContext;
//        this.tenantService = tenantService;
        setSpacing(false);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassNames(LumoUtility.Padding.XLARGE);
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
//        if (StringUtils.hasText(TenantContext.getCurrentTenant())) {
//            event.forwardTo("persons");
//        }
//
//        Optional<CustomUserDetails> commonUsers = authenticationContext.getAuthenticatedUser(UserDetails.class)
//                .map(ud -> (CustomUserDetails) ud);

        var variants = new ButtonVariant[]{ButtonVariant.LUMO_PRIMARY};
        var width = "200px";
        var height = "100px";

//        commonUsers.ifPresent(user -> {
//
//            if (user.getTenants().size() == 1) {
//                VaadinSession.getCurrent().setAttribute(Tenant.class, user.getTenants().iterator().next());
//
//                event.forwardTo("persons");
//            } else {
//
//                user.getTenants().forEach(tenant -> {
        Button component = new Button("Test");
//                    Button component = new Button(tenant.getName());
        component.addThemeVariants(variants);
        component.setWidth(width);
        component.setHeight(height);
        component.addClickListener(e -> {
            Tenant tenant = new Tenant();
            tenant.setTenantId("tenant_01");
            tenant.setName("Test");

//            try {
//                Connection connection = dataSource.getConnection();
//                connection.setSchema(tenant.getTenantId());
//
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }

            VaadinSession.getCurrent().setAttribute(Tenant.class, tenant);

            UI.getCurrent().navigate(EmptyView.class);
        });
        add(component);
//                });
//
//            }
//
//        });
    }
}
