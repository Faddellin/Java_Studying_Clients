package org.example.clientsbackend.Application.Models.AddressModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Schema(description = "Address model")
public class AddressModel {

    @NotNull
    @Schema(description = "Address id", example = "76423")
    private Long id;

    @NotNull
    @Schema(description = "City part of the address", example = "Voronezh")
    private String city;

    @NotNull
    @Schema(description = "Street part of the address", example = "Lenina street")
    private String street;
}
