package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.inject.Inject;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.repositories.common.UserRepository;
import org.jconfdominicana.utlis.NotificationUtils;

import java.time.LocalDate;


@AnonymousAllowed
@PageTitle("Sing Up")
@Route(value = "singup")
public class SingUpView extends Div {
    private static final String COLOR = "color";

    private final TextField username = new TextField("Username");
    private final PasswordField password1 = new PasswordField("Password");
    private final PasswordField password2 = new PasswordField("Repeat password");
    private final Binder<User> binder;

    private final String version = "1.0.0";
    private final String patternPassword = "^(?=.*[0-9])(?=.*[a-zA-Z]).{5}.*$";
    @Inject
    UserRepository userRepository;
    @Inject
    NotificationUtils notification;

    private User user;

    public SingUpView() {
        addClassName("singup-view");

        setSizeFull();
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

        binder = new Binder<>(User.class);

        validatedPasswordField(password1);
        validatedPasswordField(password2);

//        BcryptUtil.bcryptHash("admin").ma

        binder.forField(password1)
                .withValidator(value -> (value != null && !value.isEmpty()) && value.matches(patternPassword), "It is not a valid password.")
                .withValidator(value -> value.equals(password2.getValue()), "The passwords do not match.")
                .bind(u -> null, (u, s) -> {
                });

        binder.forField(password2)
                .withValidator(value -> (value != null && !value.isEmpty()) && value.matches(patternPassword), "It is not a valid password.")
                .withValidator(value -> value.equals(password1.getValue()), "The passwords do not match.")
                .bind(u -> null, (u, s) -> {
                });


        Span headerLabel = new Span();
        headerLabel.setText("Create your account");
        Span detailsLabel = new Span();
        detailsLabel.setText("Please enter your details.");
        Span accountLabel = new Span();
        accountLabel.setText("Already have an account?");

        Span copyrightLabel = new Span();
        copyrightLabel.setText("© %s JConf Dom LLC. All rights reserved.".formatted(LocalDate.now().getYear()));
        Span versionLabel = new Span();
        versionLabel.setText("Version: %s".formatted(version));
        Span contactLabel = new Span();
        contactLabel.setText("Contact us at support@jconfdom.org if you're experiencing issues logging into your account.");
//
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

        Anchor loginLink = createAnchor("login", "Log In");
        loginLink.addClassNames(LumoUtility.TextColor.HEADER);

        HorizontalLayout accountLayout = new HorizontalLayout(accountLabel, loginLink);
        accountLayout.addClassNames(LumoUtility.FlexWrap.WRAP);

        Checkbox policyCheckbox = new Checkbox();
        policyCheckbox.addClassNames(LumoUtility.Margin.MEDIUM, LumoUtility.AlignContent.CENTER);
        policyCheckbox.getStyle()
                .set("margin-right", "0px !important")
                .set("margin-bottom", "0px !important");

        Span creatingLabel = new Span("By creating, you are agreeing to our ");
        creatingLabel.getStyle().setMarginInlineStart("0").setMarginInlineEnd("0").set("margin-block-start", "1em").set("margin-block-end", "1em");
        creatingLabel.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);
        creatingLabel.getStyle().set("margin-bottom", "0px !important");

        Anchor termsLink = createAnchor("#", "Terms of Service");
        termsLink.addClassNames(LumoUtility.TextColor.HEADER);
        termsLink.getStyle().set("margin-bottom", "0px !important");

        Anchor policyLink = createAnchor("#", "Privacy Policy");
        policyLink.addClassNames(LumoUtility.TextColor.HEADER);
        policyLink.getStyle().set("margin-top", "0px !important");

        HorizontalLayout creatingLayout = new HorizontalLayout(policyCheckbox, creatingLabel, termsLink, policyLink);
        creatingLayout.addClassNames(LumoUtility.FlexWrap.WRAP, LumoUtility.JustifyContent.CENTER);

        Button singup = new Button("Sing Up");
        singup.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        singup.setHeightFull();

        versionLabel.getStyle().setFontWeight("500").set("font-style", "italic");

        HorizontalLayout copyrightLayout = new HorizontalLayout(copyrightLabel, versionLabel);
        copyrightLayout.setWidthFull();
        copyrightLayout.addClassNames(LumoUtility.FlexWrap.WRAP);
        copyrightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout footerLayout = new VerticalLayout(/*loginWith(),*/ copyrightLayout, contactLabel);
        footerLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Margin.Bottom.LARGE);
        footerLayout.addClassNames(LumoUtility.Gap.MEDIUM);

        Div section = new Div(img, headerLabel, detailsLabel, username, password1, password2, creatingLayout, singup, accountLayout, footerLayout);
        section.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Height.AUTO);
        section.addClassNames(LumoUtility.MaxWidth.FULL, LumoUtility.Padding.LARGE, LumoUtility.Padding.Bottom.NONE);
        section.addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.Background.BASE, LumoUtility.Margin.SMALL);
        section.getStyle()
                .setWidth("30rem")
                .setBorderRadius("2.5rem");


        add(section);

//


    }

    private void validatedPasswordField(PasswordField password) {
        password.setClearButtonVisible(true);
        password.setMaxLength(100);
        password.setRequiredIndicatorVisible(true);
        password.setPattern(patternPassword);
        password.setRevealButtonVisible(false);

        Icon icon = VaadinIcon.CHECK.create();
        icon.setVisible(false);
        icon.getStyle().set(COLOR, "var(--lumo-success-color)");
        password.setSuffixComponent(icon);

        Span strengthText = new Span();
        Div strength = new Div();
        strength.add(strengthText);

        strength.add(new Text("Password security"), strengthText);
        password.setHelperComponent(strength);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        password.addValueChangeListener(e -> updateHelper(e.getValue(), strengthText, icon));

    }

    private void updateHelper(String password, Span passwordStrengthText, Icon checkIcon) {
        if (password.length() > 9 && password.matches(patternPassword)) {
            passwordStrengthText.setText("strong");
            passwordStrengthText.getStyle().set(COLOR, "var(--lumo-success-color)");
            checkIcon.setVisible(true);
        } else if (password.length() >= 5 && password.matches(patternPassword)) {
            passwordStrengthText.setText("moderate");
            passwordStrengthText.getStyle().set(COLOR, "var(--lumo-warning-color)");
            checkIcon.setVisible(false);
        } else if (password.isEmpty()) {
            passwordStrengthText.setText(null);
            checkIcon.setVisible(false);
        } else {
            passwordStrengthText.setText("weak");
            passwordStrengthText.getStyle().set(COLOR, "var(--lumo-error-color)");
            checkIcon.setVisible(false);
        }
    }


    private Anchor createAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);

        anchor.getStyle().setTextDecoration("underline").set("margin-block-start", "1em").set("margin-block-end", "1em");

        anchor.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);
        anchor.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        return anchor;
    }
}
