package org.example.clientsbackend.Application.Repositories.Interfaces;

import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Domain.Entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends
        BaseRepository<Client, Long>
{
    Optional<Client> findByEmail(String email);
    List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel);
    Integer getClientsCountByFilters(ClientFiltersModel clientFiltersModel);
}
