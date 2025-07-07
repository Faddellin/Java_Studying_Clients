package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Client.ClientPhoneUpdateModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Domain.Entities.Client;

import java.util.List;

public interface ClientService {
    void saveClient(Client client);
    void deleteClient(Long clientId) throws ExceptionWrapper;
    void updateClient(Long clientId, ClientEditModel clientEditModel) throws ExceptionWrapper;
    void updateClientPhoneNumber(ClientPhoneUpdateModel clientPhoneUpdateModel) throws ExceptionWrapper;
    ClientPagedListModel getClients(ClientFiltersModel clientFiltersModel);
    void assignManagerToCleint (Long clientId, Long managerId) throws ExceptionWrapper;
    List<ClientModel> getClientsByManagerId (Long managerId) throws ExceptionWrapper;
}
