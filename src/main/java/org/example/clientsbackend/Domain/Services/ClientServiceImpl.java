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
import org.example.clientsbackend.Application.Repositories.Interfaces.AddressRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.ManagerRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Mappers.AddressMapper;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository _clientRepository;
    private final ManagerRepository _managerRepository;
    private final AddressRepository _addressRepository;

    public ClientServiceImpl(ClientRepository clientRepository, ManagerRepository managerRepository, AddressRepository addressRepository) {
        _clientRepository = clientRepository;
        _managerRepository = managerRepository;
        _addressRepository = addressRepository;
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

    public void deleteClient(Long clientId) throws ExceptionWrapper {
        Optional<Client> clientO = _clientRepository.findById(clientId);

        if (clientO.isEmpty()) {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("Client", "Client is not exists");
            throw entityNotFoundException;
        }

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
        if (clientEditModel.getAddressCreateModel() != null) {
            if (client.getAddress() != null) {
                Long oldAddressId = client.getAddress().getId();
                client.setAddress(null);
                _clientRepository.flush();
                _addressRepository.deleteById(oldAddressId);
            }
            client.setAddress(AddressMapper.INSTANCE.addressCreateModelToAddress(clientEditModel.getAddressCreateModel()));
        }
        _clientRepository.flush();
        _addressRepository.flush();
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

    public void assignManagerToCleint (Long clientId, Long managerId) throws ExceptionWrapper{
        Optional<Client> clientO = _clientRepository.findById(clientId);
        Optional<Manager> managerO = _managerRepository.findById(managerId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            if(clientO.isEmpty()){
                entityNotFoundException.addError("clientId", "Client is not exists");
            }
            if(managerO.isEmpty()){
                entityNotFoundException.addError("managerId", "Manager is not exists");
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        clientO.get().setManager(managerO.get());
        _clientRepository.flush();
    }

    public List<ClientModel> getClientsByManagerId (Long managerId) throws ExceptionWrapper{
        Optional<Manager> managerO = _managerRepository.findById(managerId);

        if(managerO.isEmpty()){
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            entityNotFoundException.addError("managerId", "Manager is not exists");
            throw entityNotFoundException;
        }

        return _clientRepository.findAllByManager(managerO.get()).stream()
                .map(ClientMapper.INSTANCE::clientToClientModel)
                .toList();

    }
}
