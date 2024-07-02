package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticationNavigatation);
        });
    }

    private void authenticationNavigatation(BeforeEnterEvent event) {
        event.rerouteTo("/login");
    }
}
