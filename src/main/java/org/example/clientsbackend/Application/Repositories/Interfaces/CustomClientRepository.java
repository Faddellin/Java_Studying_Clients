package org.example.clientsbackend.Application.Repositories.Interfaces;

import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomClientRepository{
    List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel);
}
