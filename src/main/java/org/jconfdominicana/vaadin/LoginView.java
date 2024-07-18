package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.quarkus.annotation.UIScoped;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jconfdominicana.security.vaadin.SecurityService;
import org.jconfdominicana.utlis.NotificationUtils;

import java.time.LocalDate;


@UIScoped
@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final Anchor anchor = new Anchor();

    @Inject
    SecurityService securityService;
    @Inject
    NotificationUtils notification;
    public LoginView(
            @ConfigProperty(name = "quarkus.application.version", defaultValue = "unknown") String version,
            @ConfigProperty(name = "application.label.copyright", defaultValue = "unknown") String copyright,
            @ConfigProperty(name = "application.label.contact", defaultValue = "unknown") String contact
    ) {
        setAction("/j_security_check");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());

        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setUsername("Username");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Sign In");
        i18n.getForm().setForgotPassword("Did you forget your password?");

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Incorrect username or password");
//        errorMessage.setMessage("Try again or contact the administrator.");
        errorMessage.setMessage("Check that you have entered the correct username and password and try again or contact the administrator.");
        i18n.setErrorMessage(errorMessage);

        setI18n(i18n);

        Span headerLabel = new Span();
        headerLabel.setText("Welcome back!");
        Span detailsLabel = new Span();
        detailsLabel.setText("Please enter your details.");
        Span accountLabel = new Span();
        accountLabel.setText("Doesn`t have an account yet?");
        anchor.setText("Sign Up");
//
        Span copyrightLabel = new Span();
        copyrightLabel.setText(copyright.formatted(LocalDate.now().getYear()));
        Span versionLabel = new Span();
        versionLabel.setText("Version: %s".formatted(version));
        Span contactLabel = new Span();
        contactLabel.setText(contact);

        Image img = new Image("icons/icon.png", "Logo");
        img.setWidth("100px");
        img.addClassNames(LumoUtility.AlignSelf.CENTER, LumoUtility.Margin.Top.SMALL);

        headerLabel.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.XXLARGE, LumoUtility.TextAlignment.CENTER);
        headerLabel.addClassNames(LumoUtility.TextColor.HEADER);

        detailsLabel.addClassNames(LumoUtility.FontWeight.LIGHT, LumoUtility.FontSize.LARGE, LumoUtility.TextAlignment.CENTER);
        detailsLabel.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.TextColor.HEADER);

        accountLabel.addClassNames(LumoUtility.TextColor.HEADER);
        accountLabel.getStyle().setMarginInlineStart("0").setMarginInlineEnd("0").set("margin-block-start", "1em").set("margin-block-end", "1em");
        accountLabel.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);

        Anchor singUpLink = createAnchor("singup");
        singUpLink.addClassNames(LumoUtility.TextColor.HEADER);

        HorizontalLayout accountLayout = new HorizontalLayout(accountLabel, singUpLink);
        accountLayout.addClassNames(LumoUtility.FlexWrap.WRAP);

        Div section = new Div(img, headerLabel, detailsLabel, accountLayout);
        section.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Height.AUTO);
        section.addClassNames(LumoUtility.MaxWidth.FULL, LumoUtility.Padding.LARGE, LumoUtility.Padding.Bottom.NONE);
        section.getStyle().setBackground("var(--lumo-base-color)");

        setTitle(section);

        setForgotPasswordButtonVisible(true);

        versionLabel.getStyle().setFontWeight("500").set("font-style", "italic");

        HorizontalLayout copyrightLayout = new HorizontalLayout(copyrightLabel, versionLabel);
        copyrightLayout.setWidthFull();
        copyrightLayout.addClassNames(LumoUtility.FlexWrap.WRAP);
        copyrightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout footerLayout = new VerticalLayout(/*loginWith(),*/ copyrightLayout, contactLabel);
        footerLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Padding.Bottom.LARGE);
        footerLayout.addClassNames(LumoUtility.Gap.MEDIUM);

        getFooter().add(footerLayout);

        setOpened(true);

        addForgotPasswordListener(forgotPasswordEvent -> notification.warning("Waiting for implementation"));

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

    private Anchor createAnchor(String href) {
        anchor.setHref(href);

        anchor.getStyle().setTextDecoration("underline").set("margin-block-start", "1em").set("margin-block-end", "1em");

        anchor.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);
        anchor.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        return anchor;
    }
}
