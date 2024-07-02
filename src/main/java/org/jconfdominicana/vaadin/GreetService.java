package org.jconfdominicana.vaadin;

import jakarta.enterprise.context.Dependent;

@Dependent
public class GreetService {

    public String greet(String name, String role) {
        if (name == null || name.isEmpty()) {
            return "Hello anonymous user";
        } else {
            return "The username logged is: " + name + " and the role assigned is " + role;
        }
    }
}
