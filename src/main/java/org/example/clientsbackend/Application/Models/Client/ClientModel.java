package org.example.clientsbackend.Application.Models.Client;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.AddressModel.AddressModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Schema(description = "Client model")
public class ClientModel {

    @NotNull
    @Schema(description = "Client id", example = "623554")
    private Long id;

    @NotBlank
    @Schema(description = "Client username", example = "ExampleName")
    private String username;

    @Email
    @Schema(description = "Client email", example = "ExampleUserEmail@gmail.com")
    private String email;

    private Integer phoneNumber;

    @NotNull
    @Schema(description = "Client age", example = "30")
    private Integer age;

    private ManagerModel managerModel;

    private AddressModel addressModel;

}
