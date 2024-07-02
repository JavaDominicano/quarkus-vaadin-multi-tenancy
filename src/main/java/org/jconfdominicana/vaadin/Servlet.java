package org.jconfdominicana.vaadin;

import com.vaadin.flow.server.*;
import com.vaadin.quarkus.QuarkusVaadinServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import org.jboss.logging.Logger;

@WebServlet(urlPatterns = "/*", name = "VaadinFlowServlet", asyncSupported = true, initParams = {
        @WebInitParam(name = "org.atmosphere.websocket.suppressJSR356", value = "false"),
})
public class Servlet extends QuarkusVaadinServlet implements SessionInitListener {

    public static final Logger LOG = Logger.getLogger(Servlet.class.getName());



    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSource()
                .addSessionInitListener(initEvent -> {
                    LOG.info("A new session has been initialized");
                });

        event.getSource()
                .addUIInitListener(initEvent -> {
                    LOG.info("A new UI has been initialized");
                });
    }
}
