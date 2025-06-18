package org.example.clientsbackend.Application.Models.Client;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.AddressModel.AddressCreateModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ClientEditModel {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotNull
    private Integer age;

    private AddressCreateModel addressCreateModel;
}
