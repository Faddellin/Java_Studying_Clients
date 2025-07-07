package org.example.clientsbackend.Application.Models.Client;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.clientsbackend.Application.Models.AddressModel.AddressCreateModel;
import org.example.clientsbackend.Application.Models.User.UserCreateModel;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Schema(description = "Client create model", name = "ClientCreateModel")
@SuperBuilder
public class ClientCreateModel
        extends UserCreateModel {

    @NotNull
    @Schema(description = "Client age", example = "26")
    private Integer age;

    @Valid
    private AddressCreateModel addressCreateModel;

}
