package io.github.manoelpiovesan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.manoelpiovesan.entities.abstracts.AbstractFullEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "papers")
@NamedQuery(
        name = "Paper.search",
        query = "SELECT p FROM Paper p WHERE p.title LIKE CONCAT('%', :term, '%') OR p.summary LIKE CONCAT('%', :term, '%')")
@SQLDelete(sql = "UPDATE papers SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Paper extends AbstractFullEntity {

    @Column(name = "title", nullable = false, length = 1500)
    public String title;

    @Column(name = "summary", nullable = false, length = 5000)
    public String summary;

    // User <1 -- *> Paper
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @JsonIgnore
    String term = null;
}
