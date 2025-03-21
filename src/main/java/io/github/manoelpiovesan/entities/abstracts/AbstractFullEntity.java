package io.github.manoelpiovesan.entities.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;

/**
 * @author Manoel Rodrigues
 */
@MappedSuperclass
public abstract class AbstractFullEntity extends AbstractEntity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    public Date createdAt = new Date();

    @JsonIgnore
    @Column(
            name = "updated_at",
            nullable = false
    )
    public Date updatedAt = new Date();

    @JsonIgnore
    @Column(
            name = "deleted_at"
    )
    public Date deletedAt = null;

}