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
        Client sourceClient = Client.builder()
                .username("testName")
                .id(1L)
                .email("testEmail@mail.ru")
                .age(15)
                .phoneNumber(141235)
                .build();
        Manager sourceManager = Manager.builder()
                .username("managerName")
                .id(2L)
                .email("testManagerEmail@mail.ru")
                .phoneNumber(5425234)
                .build();
        Address sourceAddress = new Address(3L, "testCity", "randomStreet");
        sourceClient.setManager(sourceManager);
        sourceClient.setAddress(sourceAddress);

        ClientModel clientModel = ClientMapper.INSTANCE.clientToClientModel(sourceClient);

        assertNotNull(clientModel);
        assertEquals(clientModel.getId(), sourceClient.getId());
        assertEquals(clientModel.getPhoneNumber(), sourceClient.getPhoneNumber());
        assertEquals(clientModel.getUsername(), sourceClient.getUsername());
        assertEquals(clientModel.getEmail(), sourceClient.getEmail());
        assertEquals(clientModel.getAge(), sourceClient.getAge());
        assertTrue(
                clientModel.getManagerModel().getId().equals(sourceManager.getId()) &&
                        clientModel.getManagerModel().getUsername().equals(sourceManager.getUsername()) &&
                        clientModel.getManagerModel().getPhoneNumber().equals(sourceManager.getPhoneNumber()) &&
                        clientModel.getManagerModel().getEmail().equals(sourceManager.getEmail())
        );
        assertTrue(
                clientModel.getAddressModel().getId().equals(sourceAddress.getId()) &&
                        clientModel.getAddressModel().getCity().equals(sourceAddress.getCity()) &&
                        clientModel.getAddressModel().getStreet().equals(sourceAddress.getStreet())
        );
    }

    @Test
    void ClientCreateModelToDomain_Normal_returnClient(){
        ClientCreateModel clientCreateModel = ClientCreateModel
                .builder()
                .username("testName")
                .age(15)
                .email("test@test.com")
                .addressCreateModel(new AddressCreateModel("testCity", "testStreet"))
                .build();

        Client result = ClientMapper.INSTANCE.clientCreateModelToClient(clientCreateModel, "randomHash");

        assertNotNull(result);
        assertEquals(result.getEmail(), clientCreateModel.getEmail());
        assertEquals(result.getUsername(), clientCreateModel.getUsername());
        assertEquals(result.getAge(), clientCreateModel.getAge());
        assertEquals(result.getAddress().getCity(), clientCreateModel.getAddressCreateModel().getCity());
        assertEquals(result.getAddress().getStreet(), clientCreateModel.getAddressCreateModel().getStreet());
    }
}
