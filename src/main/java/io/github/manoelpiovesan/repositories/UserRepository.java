package io.github.manoelpiovesan.repositories;

import io.github.manoelpiovesan.entities.User;
import io.github.manoelpiovesan.utils.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    /**
     * Create a new user
     *
     * @param user User
     * @return User
     */
    @Transactional
    public User create(User user) {

        User persistedUser = findByEmail(user.email);

        if (persistedUser != null) {
            return null;
        }

        User validatedUser = validateUser(user);

        if (validatedUser == null) {
            return null;
        }

        validatedUser.password = BCrypt.hashpw(validatedUser.password, BCrypt.gensalt());

        persist(validatedUser);

        System.out.println("[ETY] Creating user: " + validatedUser.username);

        return validatedUser;
    }

    /**
     * Find a user by email
     *
     * @param email String
     * @return User
     */
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    /**
     * Check if the password is correct
     *
     * @param loginCredentials LoginCredentials
     * @return User
     */
    public User checkPassword(io.github.manoelpiovesan.utils.LoginCredentials loginCredentials) {
        User user = findByEmail(loginCredentials.email);


        if (user == null) {
            return null;
        }

        if (BCrypt.checkpw(loginCredentials.password, user.password)) {
            return user;
        }

        return null;
    }

    /**
     * Validate the user
     *
     * @param user User
     * @return User
     */
    private User validateUser(User user) {
        if (user.username == null || user.username.isBlank()) {
            return null;
        }

        if (user.firstName == null || user.firstName.isBlank()) {
            return null;
        }

        if (user.lastName == null || user.lastName.isBlank()) {
            return null;
        }

        if (user.password == null || user.password.isBlank()) {
            return null;
        }

        if (user.email == null || user.email.isBlank()) {
            return null;
        } else if (!user.email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            return null;
        }

        return user;
    }

    /**
     * Update the user
     *
     * @param user User
     * @return User
     */
    @Transactional
    public User update(User user) {
        User persistedUser = findById(user.id);

        if (persistedUser == null) {
            return null;
        }

        persistedUser.username = user.username;
        persistedUser.firstName = user.firstName;
        persistedUser.lastName = user.lastName;
        persistedUser.email = user.email;
        persistedUser.role = Role.USER;
        persistedUser.updatedAt = new Date();

        if (user.password != null && !user.password.isBlank()) {
            persistedUser.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        }

        persist(persistedUser);

        return persistedUser;
    }
}