package org.example.clientsbackend.Application.Models.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Schema(description = "Admin create model")
public class AdminCreateModel
        extends UserCreateModel{

}
