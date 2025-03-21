package io.github.manoelpiovesan.repositories;

import io.github.manoelpiovesan.entities.Paper;
import io.github.manoelpiovesan.entities.User;
import io.github.manoelpiovesan.projections.PaperProjection;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class PaperRepository extends AbstractRepository<Paper> {


    @Inject
    UserRepository userRepository;

    /**
     * Search papers
     *
     * @param term String
     * @return PanacheQuery<Paper>
     */
    @Override
    PanacheQuery<Paper> search(String term) {
        if (term == null || term.isBlank()) {
            return findAll();
        }

        return find("title LIKE ?1 OR summary LIKE ?1", "%" + term + "%");
    }


    /**
     * Search projected papers
     *
     * @param term String
     * @return PanacheQuery<PaperProjection>
     */
    public PanacheQuery<PaperProjection> searchProjected(String term, String author) {
        StringBuilder query = new StringBuilder("SELECT p.title, p.summary, CONCAT(u.firstName, ' ',u.lastName), u.email, p.createdAt FROM Paper p JOIN p.user u");
        Map<String, Object> params = Map.of();
        List<String> conditions = new ArrayList<>();

        /// Term
        if (term != null && !term.isBlank()) {
            conditions.add("p.title LIKE :term");
            conditions.add("p.summary LIKE :term");
            conditions.add("u.firstName LIKE :term");
            params = Map.of("term", "%" + term + "%");
        }

        /// Author
        if (author != null && !author.isBlank()) {
            conditions.add("u.firstName LIKE :author");
            conditions.add("u.lastName LIKE :author");
            conditions.add("CONCAT(u.firstName, ' ',u.lastName) LIKE :author");
            params = Map.of("author", "%" + author + "%");
        }

        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" OR ", conditions));
        }
        query.append(" ORDER BY p.createdAt DESC");

        return find(query.toString(), params).project(PaperProjection.class);
    }


    /**
     * List all papers projected
     *
     * @param term String
     * @return List<Paper>
     */
    public List<PaperProjection> listAllProjected(String term, String author,  int page, int size) {
        return searchProjected(term, author).page(page, size).list();
    }

    /**
     * List all papers
     *
     * @param term String
     * @return List<Paper>
     */
    public List<Paper> listAll(String term, int page, int size) {
        return search(term).page(page, size).list();
    }


    /**
     * Create a new paper
     *
     * @param paper Paper
     * @return Paper
     */
    @Transactional
    public Paper create(Paper paper, JsonWebToken jwt) {

        Paper toPersist = validatePaper(paper);

        User user = getUserFromJwt(jwt);

        if (user == null || toPersist == null) {
            throw new WebApplicationException("Invalid data", 400);
        }

        toPersist.user = user;

        persist(toPersist);

        System.out.println("[ETY] Creating paper: " + paper.title);

        return paper;
    }

    /**
     * Validate the paper
     *
     * @param paper Paper
     * @return Paper
     */
    private Paper validatePaper(Paper paper) {
        if (paper.title == null || paper.title.isBlank()) {
            return null;
        }

        if (paper.summary == null || paper.summary.isBlank()) {
            return null;
        }

        return paper;
    }

    /**
     * Delete a paper
     *
     * @param id Long
     * @return boolean
     */
    @Transactional
    public boolean delete(Long id, JsonWebToken jwt) {
        User user = getUserFromJwt(jwt);

        if (user == null) {
            throw new WebApplicationException("Invalid data", 400);
        }

        Paper paper = findById(id);

        if (paper == null) {
            throw new WebApplicationException("Paper not found", 404);
        }

        if (!paper.user.id.equals(user.id)) {
            throw new WebApplicationException("Unauthorized", 401);
        }

        delete("id", id);

        System.out.println("[ETY] Deleting paper: " + paper.title);

        return true;
    }


    /**
     * Get the user (author) from the JWT
     *
     * @param jwt JsonWebToken
     * @return User
     */
    private User getUserFromJwt(JsonWebToken jwt) {
        return userRepository.findById(Long.parseLong(jwt.getSubject()));
    }



    /**
     * Create without JWT
     * Note: This method is not used in the application.
     * Just for mocking purposes in Startup.java
     *
     * @param paper Paper
     * @return Paper
     */
    @Transactional
    public Paper create(Paper paper) {
        Paper toPersist = validatePaper(paper);

        if (toPersist == null) {
            throw new WebApplicationException("Invalid data", 400);
        }

        persist(toPersist);

        System.out.println("[ETY] Creating paper: " + paper.title);

        return paper;
    }


}
