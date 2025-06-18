package org.example.clientsbackend.unit.Mappers;

import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Mappers.ManagerMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerMapperTests {

    @Test
    void ManagerToDto_Normal_returnManagerModel(){
        Long id = 1L;
        String fullName = "testName";
        Integer phoneNumber = 891614556;
        Manager manager = new Manager(id, fullName, phoneNumber);

        ManagerModel managerModel = ManagerMapper.INSTANCE.managerToManagerModel(manager);

        assertNotNull(managerModel);
        assertEquals(managerModel.getId(), id);
        assertEquals(managerModel.getFullName(), fullName);
        assertEquals(managerModel.getPhoneNumber(), phoneNumber);
    }
}
