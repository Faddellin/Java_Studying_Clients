package org.example.clientsbackend.Application.Models.Client;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.AddressModel.AddressModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Manager;

@AllArgsConstructor
@Getter @Setter
public class ClientModel {

    //Constructor for tests
    public ClientModel(Long id, String name, String email, Integer age){
        this.id = id; this.name = name; this.email = email; this.age = age;
    }

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    private Integer age;

    private ManagerModel managerModel;

    private AddressModel addressModel;

}
