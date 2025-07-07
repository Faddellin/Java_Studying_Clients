package org.example.clientsbackend.unit.Services;

import jakarta.persistence.EntityNotFoundException;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.*;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientSortCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.example.clientsbackend.Application.Repositories.Interfaces.AddressRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.UserRepository;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.example.clientsbackend.Domain.Services.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;


    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void deleteClient_Normal_ClientDeleted() throws ExceptionWrapper {
        Long clientId = 1L;
        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(new Client()));


        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void updateClient_Normal_ClientUpdated() throws ExceptionWrapper {
        Long clientId = 1L;
        String changedEmail = "testChanged@test.com";
        String changedName = "testNameChanged";
        Integer changedAge = 16;
        ClientEditModel clientEditModel = new ClientEditModel(changedName, changedEmail, changedAge, null);
        Client savedClient = Client.builder()
                .id(clientId)
                .username(changedName)
                .email(changedEmail)
                .age(changedAge)
                .build();
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(savedClient));
        when(userRepository.findByEmailOrUsername(changedEmail, changedName)).thenReturn(List.of());

        clientService.updateClient(clientId, clientEditModel);

        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).flush();
        verify(addressRepository, times(1)).flush();
    }

    @Test
    void updateClient_ClientNotFound_throwEntityNotFoundException() {
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        ExceptionWrapper exception = assertThrows(ExceptionWrapper.class,
                () -> clientService.updateClient(clientId, new ClientEditModel()));

        assertEquals(exception.getErrors().get("clientId"),"Client not found");
        assertEquals(exception.getExceptionClass(), EntityNotFoundException.class);
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void getClients_Normal_ClientsReturned() {
        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                1,
                5,
                new ClientSortModel(ClientSortCriteria.username, SortOrder.Ascending),
                List.of(new ClientFilterModel(ClientFilterCriteria.username, FilterOperator.equal, "test"))
        );
        List<Client> returningClients = List.of(
                Client.builder()
                        .id(1L)
                        .username("test")
                        .email("test@mail.ru")
                        .age(5)
                        .build()
        );
        List<ClientModel> clientsForCheck = returningClients.stream().map(ClientMapper.INSTANCE::clientToClientModel).toList();
        when(clientRepository.getClientsByFilters(clientFiltersModel)).thenReturn(returningClients);

        ClientPagedListModel clients = clientService.getClients(clientFiltersModel);

        assertIterableEquals(clients.getClientModels().stream().map(ClientModel::getId).toList(),
                clientsForCheck.stream().map(ClientModel::getId).toList());
        verify(clientRepository, times(1)).getClientsByFilters(clientFiltersModel);
    }

}