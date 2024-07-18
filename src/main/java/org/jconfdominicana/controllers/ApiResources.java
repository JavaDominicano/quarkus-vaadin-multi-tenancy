package org.jconfdominicana.controllers;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ResourceContext;
import jakarta.ws.rs.core.Context;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.controllers.api.auth.AuthResource;
import org.jconfdominicana.controllers.api.persons.PersonResource;


@Path("/api")
@RequiredArgsConstructor
public class ApiResources {


    @Context
    ResourceContext resourceContext;

    @Path("/auth")
    public AuthResource authResource() {
        return resourceContext.getResource(AuthResource.class);
    }

    @Path("/persons")
    @Authenticated
    public PersonResource personResource() {
        return resourceContext.getResource(PersonResource.class);
    }

}
