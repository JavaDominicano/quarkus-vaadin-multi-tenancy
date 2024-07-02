package org.jconfdominicana;


import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.auth.ViewAccessChecker;
import jakarta.enterprise.event.Observes;

public class ViewAccessCheckerInit {
    private final ViewAccessChecker viewAccessChecker;

    public ViewAccessCheckerInit() {
        viewAccessChecker = new ViewAccessChecker();
        viewAccessChecker.setLoginView("/login");
    }

    public void serviceInit(@Observes ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(viewAccessChecker);
        });
    }
}
