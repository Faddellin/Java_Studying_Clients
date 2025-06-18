package org.example.clientsbackend.Domain.Services;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.Models.Common.PaginationModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Mappers.AddressMapper;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository _clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        _clientRepository = clientRepository;
    }

    public void addClient(ClientCreateModel clientCreateModel) throws ExceptionWrapper {

        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        if (clientO.isPresent()) {
            ExceptionWrapper badRequestExceptionWrapper = new ExceptionWrapper(new BadRequestException());
            badRequestExceptionWrapper.addError("email", "Client with such email address already exist");
            throw badRequestExceptionWrapper;
        }

        _clientRepository
                .save(ClientMapper.INSTANCE.clientCreateModelToClient(clientCreateModel));
    }

    public void deleteClient(Long clientId){
        _clientRepository
                .deleteById(clientId);
    }

    public void updateClient(Long clientId, ClientEditModel clientEditModel) throws ExceptionWrapper {
        Optional<Client> clientO = _clientRepository
                .findById(clientId);

        if (clientO.isEmpty()) {
            ExceptionWrapper enittyNotFoundExceptionWrapper = new ExceptionWrapper(new EntityNotFoundException());
            enittyNotFoundExceptionWrapper.addError("clientId", "Client not found");
            throw enittyNotFoundExceptionWrapper;
        }

        Optional<Client> clientWithSuchEmailO = _clientRepository.findByEmail(clientEditModel.getEmail());

        if (clientWithSuchEmailO.isPresent() && !clientWithSuchEmailO.get().getId().equals(clientId)) {
            ExceptionWrapper badRequestExceptionWrapper = new ExceptionWrapper(new BadRequestException());
            badRequestExceptionWrapper.addError("email", "Client with such email address already exist");
            throw badRequestExceptionWrapper;
        }

        Client client = clientO.get();

        client.setAge(clientEditModel.getAge());
        client.setName(clientEditModel.getName());
        client.setEmail(clientEditModel.getEmail());
        if (clientEditModel.getAddressCreateModel() != null)
            client.setAddress(AddressMapper.INSTANCE.addressCreateModelToAddress(clientEditModel.getAddressCreateModel()));

        _clientRepository.save(client);
    }

    public ClientPagedListModel getClients(ClientFiltersModel clientFiltersModel){
        Integer clientsCount = _clientRepository.getClientsCountByFilters(clientFiltersModel);

        PaginationModel paginationModel = new PaginationModel(
                clientsCount,
                clientFiltersModel.getSize(),
                clientFiltersModel.getPage()
        );
        clientFiltersModel.setPage(paginationModel.getPage());

        List<ClientModel> clientModels = _clientRepository.getClientsByFilters(clientFiltersModel)
                .stream()
                .map(ClientMapper.INSTANCE::clientToClientModel)
                .toList();

        return new ClientPagedListModel(clientModels, paginationModel);
    }
}
