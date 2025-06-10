package org.example.clientsbackend.Application.Models.Client;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Manager;

@AllArgsConstructor
@Getter @Setter
public class ClientModel {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    private Integer age;

    private Manager managerModel;

    private Address addressModel;

}
