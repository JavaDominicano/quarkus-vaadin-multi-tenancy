package org.jconfdominicana.controllers.api.auth;

import io.quarkus.arc.Unremovable;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.controllers.request.LoginRequestDTO;
import org.jconfdominicana.model.common.User;
import org.jconfdominicana.services.AuthService;
import org.jconfdominicana.services.JwtService;

@Unremovable
@RequestScoped
@RequiredArgsConstructor
public class AuthResource {

    private final JwtService jwtService;
    private final AuthService authService;

    @POST()
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(@Valid LoginRequestDTO loginRequest) {
        User user = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        String accessToken = this.jwtService.generateToken(user);

        JsonObject body = new JsonObject().put("access_token", accessToken);
        return Response.status(200)
                .entity(body)
                .build();
    }
}