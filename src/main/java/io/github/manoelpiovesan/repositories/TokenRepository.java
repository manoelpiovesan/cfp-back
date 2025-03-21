package io.github.manoelpiovesan.repositories;

import io.github.manoelpiovesan.entities.User;
import io.github.manoelpiovesan.utils.LoginCredentials;
import io.github.manoelpiovesan.utils.TokenUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import net.bytebuddy.utility.RandomString;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@RequestScoped
public class TokenRepository {
    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;


    /**
     * Generate a token for the user
     *
     * @param loginCredentials LoginCredentials
     * @return String
     */
    public String generate(LoginCredentials loginCredentials) {

        User user = userRepository.checkPassword(loginCredentials);

        if (user == null) {
            throw new WebApplicationException("Invalid credentials", 401);
        }

        try {
            String token = TokenUtils.generateTokenString(getUserClaims(user));

            System.out.println("[JWT] Generating token for user: " + user.username);

            return token;
        } catch (Exception e) {

            System.out.println("[JWT] Error generating token: " + e.getMessage());
            return null;
        }

    }

    /**
     * Get all the user claims
     *
     * @param user User
     * @return JwtClaims
     */
    private JwtClaims getUserClaims(User user) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setIssuer(issuer);
        jwtClaims.setJwtId(RandomString.make(8));
        jwtClaims.setSubject(user.id.toString());
        jwtClaims.setAudience("cfp-backend");
        jwtClaims.setExpirationTimeMinutesInTheFuture(10);

        /*
          Set the user claims
         */
        jwtClaims.setClaim("firstName", user.firstName);
        jwtClaims.setClaim("lastName", user.lastName);
        jwtClaims.setClaim("username", user.username);
        jwtClaims.setClaim("email", user.email);
        jwtClaims.setClaim("user_id", user.id);
        jwtClaims.setClaim(Claims.groups.name(), List.of(user.role));


        return jwtClaims;
    }


}