package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.AddressModel.AddressModel;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Entities.Manager;

public class DomainToDtoMapper {

    public static ManagerModel MapToDto(Manager manager){
        return new ManagerModel(
                manager.getId(),
                manager.getFullName(),
                manager.getPhoneNumber()
                );
    }

    public static AddressModel MapToDto(Address address){
        return new AddressModel(
                address.getId(),
                address.getCity(),
                address.getStreet()
        );
    }

    public static ClientModel MapToDto(Client client){
        return new ClientModel(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getAge(),
                (client.getManager() != null ? MapToDto(client.getManager()) : null),
                (client.getAddress() != null ? MapToDto(client.getAddress()) : null)
        );
    }

}
