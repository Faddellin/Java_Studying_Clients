package org.example.clientsbackend.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Auth.TokenResponseModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ExampleOfExternalSystemService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ExampleOfExternalSystemController {

    private final ExampleOfExternalSystemService _exampleOfExternalSystemService;

    @Operation(
            summary = "Change user phone",
            description = "Change user phone by user id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User phone is changed",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseModel.class)
                    )}
            )
    })
    @PostMapping(path = "clients/{clientId}/changePhone")
    @SecurityRequirement(name = "JWT")
    public void ChangeClientPhoneByClientId(
            @RequestBody @Parameter(description = "Phone number", example = "98458125", required = true) Integer phoneNumber,
            @PathVariable @Parameter(description = "Client id", example = "412612", required = true) Long clientId
            ) throws ExceptionWrapper {
        _exampleOfExternalSystemService.changeClientPhoneNumber(phoneNumber, clientId);
    }
}
