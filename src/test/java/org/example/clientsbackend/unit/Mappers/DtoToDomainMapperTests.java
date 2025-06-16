package org.example.clientsbackend.unit.Mappers;

import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Mappers.DtoToDomainMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DtoToDomainMapperTests {

    @Test
    void ClientCreateModelToDomain_Normal_returnClient(){
        String email = "test@test.com";
        String name = "testName";
        Integer age = 15;
        ClientCreateModel clientCreateModel = new ClientCreateModel(name, email, age);

        Client result = DtoToDomainMapper.MapToDomain(clientCreateModel);

        assertNotNull(result);
        assertEquals(result.getEmail(), email);
        assertEquals(result.getName(), name);
        assertEquals(result.getAge(), age);
    }

}
