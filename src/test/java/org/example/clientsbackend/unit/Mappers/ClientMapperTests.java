package org.example.clientsbackend.unit.Mappers;

import org.example.clientsbackend.Application.Models.AddressModel.AddressCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientMapperTests {

    @Test
    void ClientToDto_Normal_returnClientModel(){
        Client sourceClient = new Client(1L, "testName", "testEmail@mail.ru", 15);
        Manager sourceManager = new Manager(2L, "managerName", 5425234);
        Address sourceAddress = new Address(3L, "testCity", "randomStreet");
        sourceClient.setManager(sourceManager);
        sourceClient.setAddress(sourceAddress);

        ClientModel clientModel = ClientMapper.INSTANCE.clientToClientModel(sourceClient);

        assertNotNull(clientModel);
        assertEquals(clientModel.getId(), sourceClient.getId());
        assertEquals(clientModel.getName(), sourceClient.getName());
        assertEquals(clientModel.getEmail(), sourceClient.getEmail());
        assertEquals(clientModel.getAge(), sourceClient.getAge());
        assertTrue(
                clientModel.getManagerModel().getId().equals(sourceManager.getId()) &&
                        clientModel.getManagerModel().getFullName().equals(sourceManager.getFullName()) &&
                        clientModel.getManagerModel().getPhoneNumber().equals(sourceManager.getPhoneNumber())
        );
        assertTrue(
                clientModel.getAddressModel().getId().equals(sourceAddress.getId()) &&
                        clientModel.getAddressModel().getCity().equals(sourceAddress.getCity()) &&
                        clientModel.getAddressModel().getStreet().equals(sourceAddress.getStreet())
        );
    }

    @Test
    void ClientCreateModelToDomain_Normal_returnClient(){
        String email = "test@test.com";
        String name = "testName";
        Integer age = 15;
        String city = "testCity";
        String street = "testStreet";
        ClientCreateModel clientCreateModel = new ClientCreateModel(name,
                email,
                age,
                new AddressCreateModel(city, street));

        Client result = ClientMapper.INSTANCE.clientCreateModelToClient(clientCreateModel);

        assertNotNull(result);
        assertEquals(result.getEmail(), email);
        assertEquals(result.getName(), name);
        assertEquals(result.getAge(), age);
        assertEquals(result.getAddress().getCity(), city);
        assertEquals(result.getAddress().getStreet(), street);
    }
}
