package org.example.clientsbackend.Application.Models.AddressModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Schema(description = "Model for address creating")
public class AddressCreateModel {

    @NotNull
    @Schema(description = "City part of the address", example = "Voronezh")
    private String city;

    @NotNull
    @Schema(description = "Street part of the address", example = "Lenina street")
    private String street;
}
