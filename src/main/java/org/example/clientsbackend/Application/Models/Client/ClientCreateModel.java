package org.example.clientsbackend.Application.Models.Client;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.AddressModel.AddressCreateModel;

@AllArgsConstructor
@Getter @Setter
public class ClientCreateModel {

    @NotBlank
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    private Integer age;

    @Valid
    private AddressCreateModel addressCreateModel;

}
