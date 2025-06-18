package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.AddressModel.AddressCreateModel;
import org.example.clientsbackend.Application.Models.AddressModel.AddressModel;
import org.example.clientsbackend.Domain.Entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper( AddressMapper.class );

    AddressModel addressToAddressModel(Address address);

    Address addressCreateModelToAddress(AddressCreateModel addressCreateModel);
}
