package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
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

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@RolesAllowed("admin")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    @Inject
    GreetService greetService;

    @Inject
    SecurityIdentity securityIdentity;

    public MainView(SecurityIdentity securityIdentity) {

        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.setValue(securityIdentity.getPrincipal().getName());
        textField.addThemeName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello", e -> {
//            add(new Paragraph(greetService.greet(textField.getValue())));
            Notification.show("Hello " + textField.getValue());
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in
        // shared-styles.css.
        addClassName("centered-content");

        add(textField, button);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    }
}
