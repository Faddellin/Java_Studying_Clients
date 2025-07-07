package org.example.clientsbackend.Domain.Services;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.*;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.Models.Common.PaginationModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.AddressRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.ManagerRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.UserRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Entities.User;
import org.example.clientsbackend.Domain.Mappers.AddressMapper;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository _clientRepository;
    private final UserRepository _userRepository;
    private final ManagerRepository _managerRepository;
    private final AddressRepository _addressRepository;
    private final Logger logger = LoggerFactory.getLogger("JSON_BUSINESS_LOGIC_LOGGER");

    public ClientServiceImpl(
            ClientRepository clientRepository,
            ManagerRepository managerRepository,
            AddressRepository addressRepository,
            UserRepository userRepository) {
        _clientRepository = clientRepository;
        _managerRepository = managerRepository;
        _addressRepository = addressRepository;
        _userRepository = userRepository;
    }

    public void saveClient(Client client){
        _clientRepository
                .save(client);
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

        List<User> usersWithSuchEmailOrUsername = _userRepository.findByEmailOrUsername(
                clientEditModel.getEmail(),
                clientEditModel.getUsername()
        );

        if (usersWithSuchEmailOrUsername.size() > 1 ||
                (!usersWithSuchEmailOrUsername.isEmpty() && !usersWithSuchEmailOrUsername.get(0).getId().equals(clientId))) {
            ExceptionWrapper badRequestExceptionWrapper = new ExceptionWrapper(new BadRequestException());
            badRequestExceptionWrapper.addError("clientEditModel", "Client with such email or username already exist");
            throw badRequestExceptionWrapper;
        }

        Client client = clientO.get();

        client.setAge(clientEditModel.getAge());
        client.setUsername(clientEditModel.getUsername());
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

    public void updateClientPhoneNumber(ClientPhoneUpdateModel сlientPhoneUpdateModel) throws ExceptionWrapper{
        Optional<Client> clientO = _clientRepository
                .findById(сlientPhoneUpdateModel.getClientId());

        if (clientO.isEmpty()) {
            ExceptionWrapper enittyNotFoundExceptionWrapper = new ExceptionWrapper(new EntityNotFoundException());
            enittyNotFoundExceptionWrapper.addError("clientId", "Client not found");
            throw enittyNotFoundExceptionWrapper;
        }

        Client client = clientO.get();

        client.setPhoneNumber(сlientPhoneUpdateModel.getPhoneNumber());
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

    public void assignManagerToCleint (Long clientId, Long managerId) throws ExceptionWrapper{
        Optional<Client> clientO = _clientRepository.findById(clientId);
        Optional<Manager> managerO = _managerRepository.findById(managerId);

        {
            ExceptionWrapper entityNotFoundException = new ExceptionWrapper(new EntityNotFoundException());
            if(clientO.isEmpty()){
                entityNotFoundException.addError("clientId", "Client is not exists");
                logger.error("Cannot assign manager to client with id: ({}), client is not exists", clientId);
            }
            if(managerO.isEmpty()){
                entityNotFoundException.addError("managerId", "Manager is not exists");
                logger.error("Cannot assign manager with id: ({}) to client, manager is not exists", managerId);
            }
            if(entityNotFoundException.hasErrors()){
                throw entityNotFoundException;
            }
        }

        clientO.get().setManager(managerO.get());
        _clientRepository.flush();

        logger.info("Manager with id: ({}) was assigned to client with id: ({})", managerId, clientId);
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
