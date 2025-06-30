package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import org.example.clientsbackend.Application.Repositories.Interfaces.AddressRepository;
import org.example.clientsbackend.Domain.Entities.Address;
import org.springframework.stereotype.Repository;

@Repository
public class AddressRepositoryImpl
        extends BaseRepositoryImpl<Address, Long>
        implements AddressRepository
{
    private final EntityManager _entityManager;

    public AddressRepositoryImpl(EntityManager entityManager) {
        super(Address.class, entityManager);
        _entityManager = entityManager;
    }
}
