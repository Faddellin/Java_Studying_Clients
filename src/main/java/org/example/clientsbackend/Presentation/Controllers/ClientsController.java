package org.example.clientsbackend.Presentation.Controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientsController {

    private final ClientService _clientService;

    public ClientsController(ClientService clientService) {
        _clientService = clientService;

    }

    @PostMapping(path = "clients/add")
    public void AddClient(@Valid @RequestBody ClientCreateModel clientCreateModel) {
        _clientService.addClient(clientCreateModel);
    }
    @DeleteMapping(value = "clients/{id}")
    public void DeleteClient(@PathVariable("id") Long clientId) {
        _clientService.deleteClient(clientId);
    }
    @PostMapping(path = "clients/{id}/update")
    public void UpdateClient(
            @Valid @RequestBody ClientEditModel clientEditModel,
            @PathVariable("id") Long clientId) throws ExceptionWrapper {
        _clientService.updateClient(clientId, clientEditModel);
    }

    @PostMapping(path = "clients/get-all")
    public ClientPagedListModel GetClients(@Valid @RequestBody ClientFiltersModel clientFiltersModel) {
        return _clientService.getClients(clientFiltersModel);
    }
}
