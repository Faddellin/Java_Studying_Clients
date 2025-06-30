package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import org.example.clientsbackend.Application.Repositories.Interfaces.ManagerRepository;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.springframework.stereotype.Repository;

@Repository
public class ManagerRepositoryImpl extends
        BaseRepositoryImpl<Manager, Long>
        implements ManagerRepository
{
    private final EntityManager _entityManager;

    public ManagerRepositoryImpl(EntityManager entityManager) {
        super(Manager.class, entityManager);
        _entityManager = entityManager;
    }


}
