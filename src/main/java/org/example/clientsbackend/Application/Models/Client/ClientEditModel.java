package org.example.clientsbackend.Application.Models.Client;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
@Schema(description = "Model for client editing")
public class ClientEditModel {

    @NotBlank
    @Schema(description = "New client username", example = "Vasiliy")
    private String username;

    @Email
    @NotNull
    @Schema(description = "New client email", example = "vasiliy@gmail.com")
    private String email;

    @NotNull
    @Schema(description = "New client age", example = "22")
    private Integer age;

    @Valid
    private AddressCreateModel addressCreateModel;
}
