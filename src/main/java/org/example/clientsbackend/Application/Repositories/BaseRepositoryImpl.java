package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.clientsbackend.Application.Repositories.Interfaces.BaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

public class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    public List<T> findAll(Specification<T> spec, Integer offset, Integer maxResults, Sort sort) {
        TypedQuery<T> query = getQuery(spec, sort);

        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be less than zero!");
        }
        if (maxResults < 1) {
            throw new IllegalArgumentException("Max results must not be less than one!");
        }

        query.setFirstResult(offset);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

}
