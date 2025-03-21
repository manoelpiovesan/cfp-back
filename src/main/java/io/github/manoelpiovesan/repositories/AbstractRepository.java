package io.github.manoelpiovesan.repositories;

import io.github.manoelpiovesan.entities.abstracts.AbstractEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
abstract class AbstractRepository<T extends AbstractEntity> implements PanacheRepository<T> {

    abstract PanacheQuery<T> search(String term);

    /**
     * List entities
     *
     * @param term String
     * @param page int
     * @param size int
     * @return List<T>
     */
    public List<T> list(String term, int page, int size) {
        return search(term).page(page, size).list();
    }

    /**
     * Count the number of entities
     *
     * @param term String
     * @return long
     */
    public long count(String term) {
        return search(term).count();
    }
}