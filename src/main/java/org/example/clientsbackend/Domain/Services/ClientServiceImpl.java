package org.example.clientsbackend.Domain.Services;

import jakarta.persistence.EntityNotFoundException;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Mappers.DtoToDomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository _clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        _clientRepository = clientRepository;
    }

    public void addClient(ClientCreateModel clientCreateModel){
        _clientRepository
                .save(DtoToDomainMapper.MapToDomain(clientCreateModel));
    }

    public void deleteClient(Long clientId){
        _clientRepository
                .deleteById(clientId);
    }

    public void updateClient(Long clientId, ClientEditModel clientEditModel){
        Optional<Client> clientO = _clientRepository
                .findById(clientId);

        if (clientO.isEmpty()) {
            throw new EntityNotFoundException("Client not found");
        }

        Client client = clientO.get();

        client.setAge(clientEditModel.getAge());
        client.setName(clientEditModel.getName());
        client.setEmail(clientEditModel.getEmail());

        _clientRepository.save(client);
    }

    public List<Client> getClients(ClientFiltersModel clientFiltersModel){
        return _clientRepository.getClientsByFilters(clientFiltersModel);
    }
}
