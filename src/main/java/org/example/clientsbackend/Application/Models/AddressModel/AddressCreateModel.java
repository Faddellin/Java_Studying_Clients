package org.example.clientsbackend.Application.Models.AddressModel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
public class AddressCreateModel {

    @NotNull
    private String city;

    @NotNull
    private String street;
}
