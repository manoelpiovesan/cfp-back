package io.github.manoelpiovesan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.manoelpiovesan.entities.abstracts.AbstractFullEntity;
import io.github.manoelpiovesan.utils.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User extends AbstractFullEntity {
    @Column(name = "first_name", nullable = false)
    public String firstName;

    @Column(name = "last_name", nullable = false)
    public String lastName;

    @Column(name = "username", nullable = false)
    public String username;

    @Column(name = "email", nullable = false)
    public String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    public String password;

    @JsonIgnore
    @Column(name = "role")
    public String role = Role.USER;

    // User <1 -- *> Paper
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public List<Paper> papers;


    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}