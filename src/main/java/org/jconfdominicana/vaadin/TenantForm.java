package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jconfdominicana.config.FlywayService;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.model.common.TenantUser;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.security.vaadin.CacheService;
import org.jconfdominicana.security.vaadin.Role;
import org.jconfdominicana.service.ProfileService;
import org.jconfdominicana.service.TenantService;
import org.jconfdominicana.utlis.NotificationUtils;

/**
 * @author me@fredpena.dev
 * @created 11/07/2024  - 11:59
 */
@Slf4j
public class TenantForm {

    private final TextField tenantId = new TextField("Tenant Id");
    private final TextField name = new TextField("Name");
    private final TextField slogan = new TextField("Slogan");
    private final TextField type = new TextField("Category");
    private final TextField phone = new TextField("Phone");
    private final TextField email = new TextField("Email");
    private final TextField website = new TextField("Web site");
    private final TextField address = new TextField("Address");

    private final Button cancel = new Button("Cancel", VaadinIcon.CLOSE.create());
    private final Button save = new Button("Save", VaadinIcon.HARDDRIVE_O.create());

    private final TenantService tenantService;
    private final ProfileService profileService;
    private final FlywayService flywayService;
    private final CacheService cacheService;
    private final NotificationUtils notification;
    private final BeanValidationBinder<Tenant> binder;
    @Setter
    private User user;

    private Tenant element;
    private boolean hasChanges = false;


    public TenantForm(TenantService tenantService, ProfileService profileService, FlywayService flywayService,
                      CacheService cacheService, NotificationUtils notification) {
        this.tenantService = tenantService;
        this.profileService = profileService;
        this.flywayService = flywayService;
        this.cacheService = cacheService;
        this.notification = notification;

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);


        binder = new BeanValidationBinder<>(Tenant.class);

        binder.forField(tenantId)
                .asRequired("This field is required")
                .bind(Tenant::getTenantId, Tenant::setTenantId);

        binder.forField(name)
                .asRequired("This field is required")
                .bind(Tenant::getName, Tenant::setName);

        save.addClickListener(this::saveOrUpdate);
    }

    private void saveOrUpdate(ClickEvent<Button> buttonClickEvent) {
        if (this.element == null) {
            this.element = new Tenant();
        }
        try {
            binder.writeBean(this.element);

            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Unsaved changes");
            dialog.setText("There are unsaved changes. Do you want to discard or save them?");

            dialog.setCancelable(true);
            dialog.setCancelButtonTheme(ButtonVariant.LUMO_ERROR.getVariantName());
            dialog.addCancelListener(event -> notification.contrast("Created tenant cancelled!"));

            dialog.setConfirmText("Save");
            dialog.addConfirmListener(event -> {

                TenantUser tenantUser = new TenantUser();
                tenantUser.setTenant(element);
                tenantUser.setUser(user);
                tenantUser.setDisabled(false);

                element.getUsers().add(tenantUser);

                tenantService.insert(this.element);
                flywayService.initNewTenantSchema(element.getTenantId());

                cacheService.putTenant(user.getUsername(), element);

//                Profile profile = new Profile();
//                profile.setUsername(user.getUsername());
//                profile.setName(user.getUsername());
//                profile.setRol(Role.ADMIN);
//
//                profileService.insert(profile);

                notification.success("Created tenant successfully!");

                element = null;
                binder.readBean(null);
                hasChanges = true;

            });
            dialog.open();

        } catch (ValidationException validationException) {
            notification.error(validationException);
        }
    }

    public Dialog createDialog(Runnable reload) {
        element = null;

        binder.readBean(element);
        hasChanges = false;

        var dialog = new Dialog();
        dialog.setMaxWidth("768px");
        dialog.setCloseOnOutsideClick(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setOpened(true);
        dialog.setHeaderTitle("Create new tenant");
        dialog.add(createFormLayout());
        dialog.getFooter().add(createButtonLayout());

        dialog.addDialogCloseActionListener(event -> {
            dialog.close();
            if (hasChanges) {
                reload.run();
            }
        });

        cancel.addClickListener(event -> {
            dialog.close();
            if (hasChanges) {
                reload.run();
            }
        });

        return dialog;
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.addClassNames(LumoUtility.Padding.SMALL);

        formLayout.add(tenantId, name, slogan, type, phone, email, website, address);
        return formLayout;
    }

    private Component createButtonLayout() {
        cancel.addClassNames(LumoUtility.Margin.End.AUTO);

        var buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.FlexWrap.WRAP, LumoUtility.JustifyContent.CENTER);
        buttonLayout.add(cancel, save);
        buttonLayout.setAlignItems(FlexComponent.Alignment.END);
        return buttonLayout;
    }
}
