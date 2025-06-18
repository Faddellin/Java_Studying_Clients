package org.example.clientsbackend.unit.Services;

import jakarta.persistence.EntityNotFoundException;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.*;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientSortCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.example.clientsbackend.Domain.Services.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void addClient_Normal_ClientAdded() throws ExceptionWrapper {
        Long clientId = 1L;
        String email = "test@test.com";
        String name = "testName";
        Integer age = 15;
        ClientCreateModel clientCreateModel = new ClientCreateModel(name, email, age, null);
        Client savedClient = new Client(clientId, name, email, age);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        clientService.addClient(clientCreateModel);

        verify(clientRepository, times(1)).save(argThat(client -> client.getEmail().equals(email)));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null,testName,15",
            "test@mail.ru,null,15",
            "test@mail.ru,testName,null"
    }, nullValues = "null")
    void addClient_Negative_throwIllegalArgumentException(String email, String name, Integer age) {
        ClientCreateModel clientCreateModel = new ClientCreateModel(email, name, age, null);
        when(clientRepository.save(any(Client.class)))
                .thenThrow(new IllegalArgumentException("Not null violation"));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> clientService.addClient(clientCreateModel));

        assertThat(exception.getMessage()).isEqualTo("Not null violation");
    }

    @Test
    void deleteClient_Normal_ClientDeleted() {

        Long clientId = 1L;

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
        Client savedClient = new Client(clientId, "testName", "test@test.com", 15);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(savedClient));

        clientService.updateClient(clientId, clientEditModel);

        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(argThat(
                client -> client.getEmail().equals(changedEmail) &&
                          client.getName().equals(changedName) &&
                          client.getAge().equals(changedAge)
        ));
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
                new ClientSortModel(ClientSortCriteria.name, SortOrder.Ascending),
                List.of(new ClientFilterModel(ClientFilterCriteria.name, FilterOperator.equal, "test"))
        );
        List<Client> returningClients = List.of(
                new Client(1L,"test","test@mail.ru",5)
        );
        List<ClientModel> clientsForCheck = returningClients.stream().map(ClientMapper.INSTANCE::clientToClientModel).toList();
        when(clientRepository.getClientsByFilters(clientFiltersModel)).thenReturn(returningClients);

        ClientPagedListModel clients = clientService.getClients(clientFiltersModel);

        assertIterableEquals(clients.getClientModels().stream().map(ClientModel::getId).toList(),
                clientsForCheck.stream().map(ClientModel::getId).toList());
        verify(clientRepository, times(1)).getClientsByFilters(clientFiltersModel);
    }

}