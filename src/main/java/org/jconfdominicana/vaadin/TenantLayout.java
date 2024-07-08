package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.jconfdominicana.security.vaadin.SecurityService;

public class TenantLayout extends AppLayout {

    private final transient SecurityService securityService;

    public TenantLayout(SecurityService securityService) {
        this.securityService = securityService;

        addToNavbar(createHeaderContent());
        setDrawerOpened(false);
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        Button addTenant = new Button("Add Tenant");
        addTenant.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        addTenant.addClassNames(Margin.End.AUTO);
        addTenant.addClickListener(event -> {
            if (getContent() instanceof TenantViewObserver view) {
                view.addTenant();
            }
        });

        layout.add(addTenant);


        Button signOut = new Button("Sign out");
        signOut.addClickListener(event -> securityService.logout());
        signOut.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);


        Button list = new Button(new Icon(VaadinIcon.LIST));
        list.addThemeVariants(ButtonVariant.LUMO_SMALL);
        list.addClickListener(event -> {
            if (getContent() instanceof TenantViewObserver view) {
                view.listView();
            }
        });

        Button grid = new Button(new Icon(VaadinIcon.GRID));
        grid.addThemeVariants(ButtonVariant.LUMO_SMALL);
        grid.addClassNames(Margin.Right.XLARGE);
        grid.addClickListener(event -> {
            if (getContent() instanceof TenantViewObserver view) {
                view.gridView();
            }
        });

        layout.add(list, grid, signOut);
        layout.addClassNames(Gap.SMALL);

        header.add(layout);

        return header;
    }


}
