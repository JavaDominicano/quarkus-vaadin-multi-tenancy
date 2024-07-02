package org.jconfdominicana.controllers;


import io.quarkus.vertx.http.runtime.security.annotation.HttpAuthenticationMechanism;
import jakarta.annotation.security.RolesAllowed;
import jakarta.resource.spi.AuthenticationMechanism;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/api")
public class ApiResources {

    @GET()
    @RolesAllowed("ADMIN")
    @Path("/123")
//    @HttpAuthenticationMechanism("session")
    public String hola() {
        return "Hola";
    }
}
