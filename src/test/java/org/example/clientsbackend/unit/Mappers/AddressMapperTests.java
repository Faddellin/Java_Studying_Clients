package org.example.clientsbackend.unit.Mappers;

import org.example.clientsbackend.Application.Models.AddressModel.AddressModel;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Mappers.AddressMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddressMapperTests {

    @Test
    void AddressToDto_Normal_returnAddressModel(){
        Long id = 1L;
        String city = "testCity";
        String street = "testStreet";
        Address address = new Address(id, city, street);

        AddressModel addressModel = AddressMapper.INSTANCE.addressToAddressModel(address);

        assertNotNull(addressModel);
        assertEquals(addressModel.getId(), id);
        assertEquals(addressModel.getCity(), city);
        assertEquals(addressModel.getStreet(), street);
    }
}
