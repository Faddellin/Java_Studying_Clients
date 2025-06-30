package org.example.clientsbackend.integration.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.AddressModel.AddressCreateModel;
import org.example.clientsbackend.Application.Models.Client.*;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientSortCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository _clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<ClientCreateModel> bunchOfTestClients = List.of(
            new ClientCreateModel(
                    "aaaName",
                    "aaaEmail@mail.ru",
                    10,
                    new AddressCreateModel("aaaCity", "aaaStreet")),
            new ClientCreateModel(
                    "bbbName",
                    "bbbEmail@mail.ru",
                    15,
                    new AddressCreateModel("bbbCity", "bbbStreet")),
            new ClientCreateModel(
                    "cccName",
                    "cccEmail@mail.ru",
                    20,
                    new AddressCreateModel("cccCity", "cccStreet"))
    );

    @AfterEach
    public void afterEach(){
        _clientRepository.deleteAll();
        _clientRepository.flush();
    }

    void addBunchOfTestClients(){
        bunchOfTestClients.forEach(c ->
            _clientRepository.save(ClientMapper.INSTANCE.clientCreateModelToClient(c))
        );
        _clientRepository.flush();
    }

    @Test
    void addClient_Positive_addClient() throws Exception {
        AddressCreateModel addressCreateModel = new AddressCreateModel("testCity","testStreet");
        ClientCreateModel clientCreateModel = new ClientCreateModel(
                "testName",
                "testEmail@mail.ru",
                20,
                addressCreateModel);

        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        assertTrue(clientO.isPresent());
        Client client = clientO.get();
        assertEquals(client.getAge(), clientCreateModel.getAge());
        assertEquals(client.getName(), clientCreateModel.getName());
        assertTrue(() -> {
            Address address = client.getAddress();
            return address.getCity().equals(addressCreateModel.getCity()) &&
                    address.getStreet().equals(addressCreateModel.getStreet());
        });
    }

    @ParameterizedTest()
    @CsvSource(value = {
            "null,testStreet,testName,testEmail@mail.ru,15,400",
            "testCity,null,testName,testEmail@mail.ru,15,400",
            "testCity,testStreet,null,testEmail@mail.ru,15,400",
            "testCity,testStreet,,testEmail@mail.ru,15,400",
            "testCity,testStreet,testName,null,15,400",
            "testCity,testStreet,testName,NOT_EMAIL,15,400",
            "testCity,testStreet,testName,testEmail@mail.ru,null,400",
    },nullValues = "null")
    void addClient_Negative_return400BadRequest(
            String city,
            String street,
            String name,
            String email,
            Integer age,
            Integer expectedStatusCode
    ) throws Exception {
        AddressCreateModel addressCreateModel = new AddressCreateModel(city,street);
        ClientCreateModel clientCreateModel = new ClientCreateModel(
                name,
                email,
                age,
                addressCreateModel);

        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(expectedStatusCode));

        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        assertTrue(clientO.isEmpty());
    }

    @Test
    void addClient_NegativeAddClientWithOccupiedEmail_return400BadRequest() throws Exception {
        ClientCreateModel clientCreateModel = new ClientCreateModel(
                "testName",
                "testEmail@mail.ru",
                20,
                new AddressCreateModel("testCity","testStreet"));

        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        String responseBody = mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(400)).andReturn().getResponse().getContentAsString();
        ExceptionWrapper ex = objectMapper.readValue(responseBody, ExceptionWrapper.class);

        assertEquals("Client with such email address already exist", ex.getErrors().get("email"));
        List<Client> clients = _clientRepository.findAllByEmail(clientCreateModel.getEmail());
        assertEquals(1, clients.size());
    }


    @Test
    void updateClient_Positive_updateClient() throws Exception {
        AddressCreateModel addressCreateModel = new AddressCreateModel("testCity","testStreet");
        ClientCreateModel clientCreateModel = new ClientCreateModel(
                "testName",
                "testEmail@mail.ru",
                20,
                addressCreateModel);

        AddressCreateModel addressEditedModel = new AddressCreateModel("editedCity","editedStreet");
        ClientEditModel clientEditModel = new ClientEditModel(
                "editedName",
                "editedEmail@mail.ru",
                20,
                addressEditedModel);

        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        Long clientId = _clientRepository.findByEmail(clientCreateModel.getEmail()).get().getId();
        mockMvc.perform(post("/clients/" + clientId + "/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientEditModel)
                )
        ).andExpect(status().is(200));
        Optional<Client> clientO = _clientRepository.findByEmail(clientEditModel.getEmail());

        assertTrue(clientO.isPresent());
        Client client = clientO.get();
        assertEquals(client.getAge(), clientEditModel.getAge());
        assertEquals(client.getName(), clientEditModel.getName());
        assertTrue(() -> {
            Address address = client.getAddress();
            return address.getCity().equals(addressEditedModel.getCity()) &&
                    address.getStreet().equals(addressEditedModel.getStreet());
        });
    }

    @ParameterizedTest()
    @CsvSource(value = {
            "null,testStreet,testName,testEmail@mail.ru,15,400",
            "testCity,null,testName,testEmail@mail.ru,15,400",
            "testCity,testStreet,null,testEmail@mail.ru,15,400",
            "testCity,testStreet,testName,null,15,400",
            "testCity,testStreet,testName,NOT_EMAIL,15,400",
            "testCity,testStreet,testName,testEmail@mail.ru,null,400",
    },nullValues = "null")
    void updateClient_Negative_return400BadRequest(
            String city,
            String street,
            String name,
            String email,
            Integer age,
            Integer expectedStatusCode
    ) throws Exception {
        AddressCreateModel addressEditedModel = new AddressCreateModel(city,street);
        ClientEditModel clientEditModel = new ClientEditModel(
                name,
                email,
                age,
                addressEditedModel);

        mockMvc.perform(post("/clients/" + 1 + "/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientEditModel)
                )
        ).andExpect(status().is(expectedStatusCode));
        Optional<Client> clientO = _clientRepository.findByEmail(clientEditModel.getEmail());

        assertTrue(clientO.isEmpty());
    }

    @Test
    void updateClient_NegativeClientNotExists_return404NotFound() throws Exception {
        AddressCreateModel addressEditedModel = new AddressCreateModel("editedCity","editedStreet");
        ClientEditModel clientEditModel = new ClientEditModel(
                "editedName",
                "editedEmail@mail.ru",
                20,
                addressEditedModel);

        mockMvc.perform(post("/clients/" + 1 + "/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientEditModel)
                )
        ).andExpect(status().is(404));
    }

    @Test
    void updateClient_NegativeUpdateClientWithOccupiedEmail_return400BadRequest() throws Exception {
        ClientCreateModel firstClientCreateModel = new ClientCreateModel(
                "testName",
                "testEmail@mail.ru",
                20,
                new AddressCreateModel("testCity","testStreet"));
        ClientCreateModel secondClientCreateModel = new ClientCreateModel(
                "testName",
                "testEmail1@mail.ru",
                20,
                new AddressCreateModel("testCity","testStreet"));
        ClientEditModel secondClientEditModel = new ClientEditModel(
                "editedName",
                "testEmail@mail.ru",
                20,
                new AddressCreateModel("editedCity","editedStreet"));

        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstClientCreateModel)
                )
        ).andExpect(status().is(200));
        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(secondClientCreateModel)
                )
        ).andExpect(status().is(200));
        Long clientId = _clientRepository.findByEmail(secondClientCreateModel.getEmail()).get().getId();
        mockMvc.perform(post("/clients/" + clientId + "/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(secondClientEditModel)
                )
        ).andExpect(status().is(400));
        Optional<Client> clientO = _clientRepository.findByEmail(secondClientCreateModel.getEmail());

        assertTrue(clientO.isPresent());
    }

    @Test
    void deleteClient_Positive_deleteClient() throws Exception {
        ClientCreateModel clientCreateModel = new ClientCreateModel(
                "testName",
                "testEmail@mail.ru",
                20,
                new AddressCreateModel("testCity","testStreet"));

        mockMvc.perform(post("/clients/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        Long clientId = _clientRepository.findByEmail(clientCreateModel.getEmail()).get().getId();
        mockMvc.perform(delete("/clients/" + clientId)).
                andExpect(status().is(200));

        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        assertTrue(clientO.isEmpty());
    }

    @Test
    void deleteClient_NegativeClientNotExists_return404NotFound() throws Exception {
        mockMvc.perform(delete("/clients/" + 1)).
                andExpect(status().is(404));
    }

    @Test
    void getAllCLients_Positive_getAllClients() throws Exception {
        addBunchOfTestClients();

        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                1,
                3,
                null,
                null
        );

        String responseBody = mockMvc.perform(post("/clients/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        ClientPagedListModel clientPagedListModel = objectMapper.readValue(responseBody, ClientPagedListModel.class);

        assertTrue(() ->{
            List<String> existingClientEmails = bunchOfTestClients
                    .stream()
                    .map(ClientCreateModel::getEmail)
                    .toList();
            return clientPagedListModel.getClientModels().stream().allMatch(clientModel ->
                    existingClientEmails
                            .contains(clientModel.getEmail())
            );
        });
    }

    @Test
    void getAllCLients_PositiveWithFilters_getAllClients() throws Exception {
        addBunchOfTestClients();

        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                1,
                3,
                null,
                List.of(new ClientFilterModel(ClientFilterCriteria.name, FilterOperator.contains, "aaa"))
        );

        String responseBody = mockMvc.perform(post("/clients/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        ClientPagedListModel clientPagedListModel = objectMapper.readValue(responseBody, ClientPagedListModel.class);

        assertTrue(() ->{
            if(clientPagedListModel.getClientModels().size() != 1){
                return false;
            }
            return clientPagedListModel.getClientModels().get(0).getEmail().equals("aaaEmail@mail.ru");
        });
    }

    @Test
    void getAllCLients_PositiveWithZeroFoundedClients_getAllClients() throws Exception {
        addBunchOfTestClients();

        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                1,
                3,
                null,
                List.of(new ClientFilterModel(ClientFilterCriteria.name, FilterOperator.equal, "aaa"))
        );

        String responseBody = mockMvc.perform(post("/clients/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        ClientPagedListModel clientPagedListModel = objectMapper.readValue(responseBody, ClientPagedListModel.class);

        assertEquals(0, clientPagedListModel.getClientModels().size());
    }

    @Test
    void getAllCLients_PositiveWithSorting_getAllClients() throws Exception {
        addBunchOfTestClients();

        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                1,
                3,
                new ClientSortModel(ClientSortCriteria.age, SortOrder.Descending),
                null
        );

        String responseBody = mockMvc.perform(post("/clients/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();
        ClientPagedListModel clientPagedListModel = objectMapper.readValue(responseBody, ClientPagedListModel.class);

        List<ClientCreateModel> clients = new ArrayList<>(bunchOfTestClients);
        clients.sort((client1, client2) -> client2.getAge() - client1.getAge());
        List<String> sortedClientEmails = clients
                .stream()
                .map(ClientCreateModel::getEmail)
                .toList();
        List<String> sortedRetrievedClientEmails = clientPagedListModel
                .getClientModels()
                .stream()
                .map(ClientModel::getEmail)
                .toList();
        assertArrayEquals(sortedClientEmails.toArray(), sortedRetrievedClientEmails.toArray());
    }

    @ParameterizedTest()
    @CsvSource(value = {
            "-1,3,name,Ascending,name,contains,aaaName",
            "null,3,name,Ascending,name,contains,aaaName",
            "1,0,name,Ascending,name,contains,aaaName",
            "1,null,name,Ascending,name,contains,aaaName",
            "1,3,null,Ascending,name,contains,aaaName",
            "1,3,name,null,name,contains,aaaName",
            "1,3,name,Ascending,null,contains,aaaName",
            "1,3,name,Ascending,name,null,aaaName",
    },nullValues = "null")
    void getAllCLients_NegativeWithBadClientFilterModel_return400BadRequest(
            Integer page,
            Integer pageSize,
            ClientSortCriteria sortCriteria,
            SortOrder sortOrder,
            ClientFilterCriteria filterCriteria,
            FilterOperator filterOperator,
            Object filterValue
    ) throws Exception {

        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                page,
                pageSize,
                new ClientSortModel(sortCriteria, sortOrder),
                List.of(new ClientFilterModel(filterCriteria, filterOperator, filterValue))
        );

        mockMvc.perform(post("/clients/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(400));
    }

    @Test
    void getAllCLients_NegativeWithBadClientFilterModel_return400BadRequest() throws Exception {

        ClientFiltersModel clientFiltersModel = new ClientFiltersModel(
                1,
                3,
                null,
                List.of(new ClientFilterModel(ClientFilterCriteria.age, FilterOperator.contains, "aaa"))
        );

        mockMvc.perform(post("/clients/get-all")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(400));
    }


}
