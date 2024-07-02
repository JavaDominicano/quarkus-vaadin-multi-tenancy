package org.jconfdominicana.vaadin;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@Theme("starter-theme")
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
public class AppConfig implements AppShellConfigurator {
}
