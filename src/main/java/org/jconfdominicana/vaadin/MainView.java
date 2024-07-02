package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.jconfdominicana.security.SecurityService;

import java.util.stream.Collectors;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@RolesAllowed("admin")
public class MainView extends AppLayout implements BeforeEnterObserver {

    @Inject
    GreetService greetService;

    public MainView(SecurityIdentity securityIdentity, SecurityService securityService) {

        H1 logo = new H1("Multitenancy");
        logo.addClassName("logo");
        Div div = new Div(logo);
        FlexLayout flexLayout = new FlexLayout(div);
        flexLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
//        flexLayout.setClassName(LumoUtility.Width.FULL);

        HorizontalLayout header = new HorizontalLayout(flexLayout);


        if (securityIdentity.getPrincipal() != null) {
            Button logoutButton = new Button("Logout", e -> securityService.logout());
            flexLayout.add(logoutButton);
            header = new HorizontalLayout(flexLayout);
        }

        addToNavbar(header);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    }
}
