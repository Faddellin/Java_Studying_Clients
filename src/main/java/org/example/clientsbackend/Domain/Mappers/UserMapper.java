package org.example.clientsbackend.Domain.Mappers;

import org.example.clientsbackend.Application.Models.User.UserCreateModel;
import org.example.clientsbackend.Domain.Entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(target = "passwordHash", source = "passwordHash")
    User userCreateModelToUserModel(UserCreateModel userCreateModel, String passwordHash);

}
