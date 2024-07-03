package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.inject.Inject;
import org.jconfdominicana.security.vaadin.SecurityService;


@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    @Inject
    SecurityService securityService;

    public LoginView() {
        setAction("/j_security_check");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("JConf-2024");
        i18n.getHeader().setDescription("Please enter your username and password!");

        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setTitle("Log in");
        i18n.getForm().setUsername("User");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Log in");
        i18n.getForm().setForgotPassword("have you forgotten your password?");

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Incorrect username or password.");
        errorMessage.setMessage("Please try again or contact administrator.");
        i18n.setErrorMessage(errorMessage);

        i18n.setAdditionalInformation(null);
        setI18n(i18n);

        setForgotPasswordButtonVisible(true);

        setOpened(true);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (securityService.getProfile().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));

    }
}
