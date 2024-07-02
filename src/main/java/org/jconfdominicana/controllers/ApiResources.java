package org.jconfdominicana.controllers;


import io.vertx.core.json.JsonObject;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/api")
public class ApiResources {

    @GET()
    @RolesAllowed("api")
    @Path("/123")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response json() {
        return Response.status(200)
                .entity(new JsonObject())
                .build();
    }
}
