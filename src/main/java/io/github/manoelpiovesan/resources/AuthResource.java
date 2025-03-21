package io.github.manoelpiovesan.resources;

import io.github.manoelpiovesan.entities.User;
import io.github.manoelpiovesan.repositories.TokenRepository;
import io.github.manoelpiovesan.repositories.UserRepository;
import io.github.manoelpiovesan.utils.LoginCredentials;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author Manoel Rodrigues
 */
@Path("/auth")
@RequestScoped
public class AuthResource {

    @Inject
    TokenRepository tokenRepository;

    @Inject
    UserRepository userRepository;

    /**
     * Login the user
     *
     * @param loginCredentials LoginCredentials
     * @return Response
     */
    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginCredentials loginCredentials) {
        return Response.ok(tokenRepository.generate(loginCredentials)).build();
    }

    /**
     * Create a new user
     *
     * @param user User
     * @return Response
     */
    @POST
    @Path("/register")
    @PermitAll
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        User createdUser = userRepository.create(user);

        if (createdUser == null) {
            return Response.notAcceptable(null).build();
        }

        return Response.ok(createdUser).build();
    }
}