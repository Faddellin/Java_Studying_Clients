package org.example.clientsbackend.unit.Mappers;

import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Mappers.ManagerMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerMapperTests {

    @Test
    void ManagerToDto_Normal_returnManagerModel(){
        Manager manager = Manager.builder()
                .username("testName")
                .id(1L)
                .email("testManagerEmail@mail.ru")
                .phoneNumber(5425234)
                .build();
        ManagerModel managerModel = ManagerMapper.INSTANCE.managerToManagerModel(manager);

        assertNotNull(managerModel);
        assertEquals(managerModel.getId(), manager.getId());
        assertEquals(managerModel.getUsername(), manager.getUsername());
        assertEquals(managerModel.getPhoneNumber(), manager.getPhoneNumber());
        assertEquals(managerModel.getEmail(), manager.getEmail());
    }
}
