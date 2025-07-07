package org.example.clientsbackend.Application.Models.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Model of user credentials")
public class CredentialsModel {

    @NotNull
    @Schema(description = "User username", example = "exampleUsername")
    private String username;

    @NotNull
    @Schema(description = "User password", example = "secretPassword123")
    private String password;
}
