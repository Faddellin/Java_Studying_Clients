package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ManagerMapper {
    ManagerMapper INSTANCE = Mappers.getMapper( ManagerMapper.class );

    ManagerModel managerToManagerModel(Manager manager);
}
