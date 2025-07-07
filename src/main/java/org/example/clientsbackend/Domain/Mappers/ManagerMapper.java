package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.Manager.ManagerCreateModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ManagerMapper {
    ManagerMapper INSTANCE = Mappers.getMapper( ManagerMapper.class );

    ManagerModel managerToManagerModel(Manager manager);

    @Mapping(target = "username", source = "managerCreateModel.username")
    @Mapping(target = "passwordHash", source = "passwordHash")
    Manager managerCreateModelToManager(ManagerCreateModel managerCreateModel, String passwordHash);
}
