package org.example.clientsbackend.Application.Repositories.Interfaces;

import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClientRepository extends
        BaseRepository<Client, Long>
{
    List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel);
}
