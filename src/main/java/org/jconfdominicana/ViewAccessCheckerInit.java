package org.jconfdominicana;


import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.auth.NavigationAccessControl;
import jakarta.enterprise.event.Observes;

public class ViewAccessCheckerInit {
    private final NavigationAccessControl navigationAccessControl;

    public ViewAccessCheckerInit() {
        navigationAccessControl = new NavigationAccessControl();
        navigationAccessControl.setLoginView("/login");
    }

    public void serviceInit(@Observes ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> uiInitEvent.getUI().addBeforeEnterListener(navigationAccessControl));
    }
}
