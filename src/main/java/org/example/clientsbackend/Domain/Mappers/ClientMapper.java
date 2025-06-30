package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Domain.Entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AddressMapper.class,ManagerMapper.class})
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper( ClientMapper.class );

    @Mapping(target = "managerModel", source = "manager")
    @Mapping(target = "addressModel", source = "address")
    ClientModel clientToClientModel( Client client );

    @Mapping(target = "address", source = "addressCreateModel")
    Client clientCreateModelToClient( ClientCreateModel clientCreateModel );
}
