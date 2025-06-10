package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Domain.Entities.Client;

public class DtoToDomainMapper {

    public static Client MapToDomain(ClientCreateModel clientCreateModel){
        Client client = new Client();
        client.setName(clientCreateModel.getName());
        client.setEmail(clientCreateModel.getEmail());
        client.setAge(clientCreateModel.getAge());
        return client;
    }

}
