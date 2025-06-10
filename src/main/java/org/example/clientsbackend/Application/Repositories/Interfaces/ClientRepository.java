package org.example.clientsbackend.Application.Repositories.Interfaces;

import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends
        JpaRepository<Client, Long>,
        CustomClientRepository
{
}
