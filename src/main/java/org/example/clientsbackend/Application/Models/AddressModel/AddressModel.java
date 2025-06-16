package org.example.clientsbackend.Application.Models.AddressModel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddressModel {

    @NotNull
    private Long id;

    @NotNull
    private String city;

    @NotNull
    private String street;
}
