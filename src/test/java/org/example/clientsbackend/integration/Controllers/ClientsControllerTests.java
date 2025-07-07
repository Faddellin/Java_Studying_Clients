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
import org.example.clientsbackend.Application.Models.User.AdminCreateModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Interfaces.UserRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.AuthService;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Enums.UserRole;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private UserRepository _userRepository;

    @Autowired
    private AuthService _authService;

    @Autowired
    private ObjectMapper objectMapper;

    private String accesToken;

    private final List<ClientCreateModel> bunchOfTestClients = List.of(
            ClientCreateModel
                    .builder()
                    .username("aaaName")
                    .age(10)
                    .email("aaaEmail@mail.ru")
                    .password("secret")
                    .addressCreateModel(new AddressCreateModel("aaaCity", "aaaStreet"))
                    .build(),
            ClientCreateModel
                    .builder()
                    .username("bbbName")
                    .age(10)
                    .email("bbbEmail@mail.ru")
                    .password("secret")
                    .addressCreateModel(new AddressCreateModel("bbbCity", "bbbStreet"))
                    .build(),
            ClientCreateModel
                    .builder()
                    .username("cccName")
                    .age(20)
                    .email("cccEmail@mail.ru")
                    .password("secret")
                    .addressCreateModel(new AddressCreateModel("cccCity", "cccStreet"))
                    .build()
    );

    @AfterEach
    public void afterEach(){
        _userRepository.deleteAll();
        _clientRepository.deleteAll();
        _clientRepository.flush();
        _userRepository.flush();
    }

    @BeforeEach
    public void authorizeRequest() throws ExceptionWrapper {
        AdminCreateModel userCreateModel = AdminCreateModel
                .builder()
                .username("admin")
                .email("admin@mail.ru")
                .password("admin")
                .build();
        accesToken = _authService.register(userCreateModel).getAccessToken();
    }

    void addBunchOfTestClients(){
        bunchOfTestClients.forEach(c ->
            _clientRepository.save(ClientMapper.INSTANCE.clientCreateModelToClient(
                    c,
                    _authService.getPasswordHash(c.getUsername())
            ))
        );
        _clientRepository.flush();
    }

    @Test
    void registerClient_Positive_clientRegistered() throws Exception {
        AddressCreateModel addressCreateModel = new AddressCreateModel("testCity","testStreet");

        ClientCreateModel clientCreateModel = ClientCreateModel
                .builder()
                .username("testName")
                .age(20)
                .password("secret")
                .email("testEmail@mail.ru")
                .userRole(UserRole.CLIENT)
                .addressCreateModel(addressCreateModel)
                .build();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        assertTrue(clientO.isPresent());
        Client client = clientO.get();
        assertEquals(client.getAge(), clientCreateModel.getAge());
        assertEquals(client.getUsername(), clientCreateModel.getUsername());
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
    void registerClient_Negative_return400BadRequest(
            String city,
            String street,
            String name,
            String email,
            Integer age,
            Integer expectedStatusCode
    ) throws Exception {
        AddressCreateModel addressCreateModel = new AddressCreateModel(city,street);
        ClientCreateModel clientCreateModel = ClientCreateModel
                .builder()
                .username(name)
                .age(age)
                .email(email)
                .password("secret")
                .addressCreateModel(addressCreateModel)
                .build();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(expectedStatusCode));

        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        assertTrue(clientO.isEmpty());
    }

    @Test
    void registerClient_NegativeAddClientWithOccupiedEmail_return400BadRequest() throws Exception {
        ClientCreateModel clientCreateModel = ClientCreateModel
                .builder()
                .username("testName")
                .age(20)
                .email("testEmail@mail.ru")
                .password("secret")
                .userRole(UserRole.CLIENT)
                .addressCreateModel(new AddressCreateModel("testCity","testStreet"))
                .build();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(400));

        List<Client> clients = _clientRepository.findAllByEmail(clientCreateModel.getEmail());
        assertEquals(1, clients.size());
    }


    @Test
    void updateClient_Positive_updateClient() throws Exception {
        AddressCreateModel addressCreateModel = new AddressCreateModel("testCity","testStreet");
        ClientCreateModel clientCreateModel = ClientCreateModel
                .builder()
                .username("testName")
                .age(20)
                .email("testEmail@mail.ru")
                .password("secret")
                .userRole(UserRole.CLIENT)
                .addressCreateModel(addressCreateModel)
                .build();

        AddressCreateModel addressEditedModel = new AddressCreateModel("editedCity","editedStreet");
        ClientEditModel clientEditModel = new ClientEditModel(
                "editedName",
                "editedEmail@mail.ru",
                20,
                addressEditedModel);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        Long clientId = _clientRepository.findByEmail(clientCreateModel.getEmail()).get().getId();
        mockMvc.perform(post("/clients/" + clientId + "/update")
                .header("Authorization", "Bearer " + accesToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientEditModel)
                )
        ).andExpect(status().is(200));
        Optional<Client> clientO = _clientRepository.findByEmail(clientEditModel.getEmail());

        assertTrue(clientO.isPresent());
        Client client = clientO.get();
        assertEquals(client.getAge(), clientEditModel.getAge());
        assertEquals(client.getUsername(), clientEditModel.getUsername());
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
                .header("Authorization", "Bearer " + accesToken)
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
                .header("Authorization", "Bearer " + accesToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientEditModel)
                )
        ).andExpect(status().is(404));
    }

    @Test
    void updateClient_NegativeUpdateClientWithOccupiedEmail_return400BadRequest() throws Exception {
        ClientCreateModel firstClientCreateModel = ClientCreateModel
                .builder()
                .username("testName")
                .age(20)
                .email("testEmail@mail.ru")
                .password("secret")
                .userRole(UserRole.CLIENT)
                .addressCreateModel(new AddressCreateModel("testCity","testStreet"))
                .build();
        ClientCreateModel secondClientCreateModel = ClientCreateModel
                .builder()
                .username("testName1")
                .age(20)
                .email("testEmail1@mail.ru")
                .password("secret")
                .userRole(UserRole.CLIENT)
                .addressCreateModel(new AddressCreateModel("testCity","testStreet"))
                .build();
        ClientEditModel secondClientEditModel = new ClientEditModel(
                "editedName",
                "testEmail@mail.ru",
                20,
                new AddressCreateModel("editedCity","editedStreet"));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(firstClientCreateModel)
                )
        ).andExpect(status().is(200));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(secondClientCreateModel)
                )
        ).andExpect(status().is(200));
        Long clientId = _clientRepository.findByEmail(secondClientCreateModel.getEmail()).get().getId();
        mockMvc.perform(post("/clients/" + clientId + "/update")
                .header("Authorization", "Bearer " + accesToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(secondClientEditModel)
                )
        ).andExpect(status().is(400));
        Optional<Client> clientO = _clientRepository.findByEmail(secondClientCreateModel.getEmail());

        assertTrue(clientO.isPresent());
    }

    @Test
    void deleteClient_Positive_deleteClient() throws Exception {
        ClientCreateModel clientCreateModel = ClientCreateModel
                .builder()
                .username("testName")
                .age(20)
                .email("testEmail@mail.ru")
                .password("secret")
                .userRole(UserRole.CLIENT)
                .addressCreateModel(new AddressCreateModel("testCity","testStreet"))
                .build();

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientCreateModel)
                )
        ).andExpect(status().is(200));
        Long clientId = _clientRepository.findByEmail(clientCreateModel.getEmail()).get().getId();
        mockMvc.perform(delete("/clients/" + clientId)
                .header("Authorization", "Bearer " + accesToken))
                .andExpect(status().is(200));

        Optional<Client> clientO = _clientRepository.findByEmail(clientCreateModel.getEmail());

        assertTrue(clientO.isEmpty());
    }

    @Test
    void deleteClient_NegativeClientNotExists_return404NotFound() throws Exception {
        mockMvc.perform(delete("/clients/" + 1)
                .header("Authorization", "Bearer " + accesToken))
                .andExpect(status().is(404));
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
                .header("Authorization", "Bearer " + accesToken)
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
                List.of(new ClientFilterModel(ClientFilterCriteria.username, FilterOperator.contains, "aaa"))
        );

        String responseBody = mockMvc.perform(post("/clients/get-all")
                .header("Authorization", "Bearer " + accesToken)
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
                List.of(new ClientFilterModel(ClientFilterCriteria.username, FilterOperator.equal, "aaa"))
        );

        String responseBody = mockMvc.perform(post("/clients/get-all")
                .header("Authorization", "Bearer " + accesToken)
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
                .header("Authorization", "Bearer " + accesToken)
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
            "-1,3,username,Ascending,username,contains,aaaName",
            "null,3,username,Ascending,username,contains,aaaName",
            "1,0,username,Ascending,username,contains,aaaName",
            "1,null,username,Ascending,username,contains,aaaName",
            "1,3,null,Ascending,username,contains,aaaName",
            "1,3,username,null,username,contains,aaaName",
            "1,3,username,Ascending,null,contains,aaaName",
            "1,3,username,Ascending,username,null,aaaName",
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
                .header("Authorization", "Bearer " + accesToken)
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
                .header("Authorization", "Bearer " + accesToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(clientFiltersModel)
                )
        ).andExpect(status().is(400));
    }


}
