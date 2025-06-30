package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;

import java.util.List;

public interface ClientService {
    void addClient(ClientCreateModel clientCreateModel) throws ExceptionWrapper;
    void deleteClient(Long clientId) throws ExceptionWrapper;
    void updateClient(Long clientId, ClientEditModel clientEditModel) throws ExceptionWrapper;
    ClientPagedListModel getClients(ClientFiltersModel clientFiltersModel);
    void assignManagerToCleint (Long clientId, Long managerId) throws ExceptionWrapper;
    List<ClientModel> getClientsByManagerId (Long managerId) throws ExceptionWrapper;
}
