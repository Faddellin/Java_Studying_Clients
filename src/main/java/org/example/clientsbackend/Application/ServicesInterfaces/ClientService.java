package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Domain.Entities.Client;

import java.util.List;

public interface ClientService {
    void addClient(ClientCreateModel clientCreateModel);
    void deleteClient(Long clientId);
    void updateClient(Long clientId, ClientEditModel clientEditModel) throws ExceptionWrapper;
    List<Client> getClients(ClientFiltersModel clientFiltersModel);
}
