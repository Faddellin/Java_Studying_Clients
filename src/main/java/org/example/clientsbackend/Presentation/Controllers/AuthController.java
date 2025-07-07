package org.example.clientsbackend.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Auth.CredentialsModel;
import org.example.clientsbackend.Application.Models.Auth.TokenResponseModel;
import org.example.clientsbackend.Application.Models.Common.ResponseModel;
import org.example.clientsbackend.Application.Models.User.UserCreateModel;
import org.example.clientsbackend.Application.ServicesInterfaces.AuthService;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {

    private final AuthService _authService;

    public AuthController(AuthService authService) {
        _authService = authService;
    }

    @Operation(
            summary = "Login",
            description = "Login user in system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User logged in",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseModel.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Example of error model for status codes 400-500",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    @PostMapping(path = "auth/login")
    public TokenResponseModel Login(
            @Valid @RequestBody CredentialsModel credentialsModel
    ) throws ExceptionWrapper {
        return _authService.login(credentialsModel);
    }

    @Operation(
            summary = "Register",
            description = "Register user in system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was registered",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseModel.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Example of error model for status codes 400-500",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseModel.class)
                    )}
            )
    })
    @PostMapping(value = "auth/register")
    public TokenResponseModel Register(
            @Valid @RequestBody UserCreateModel userCreateModel
    ) throws ExceptionWrapper {
        return _authService.register(userCreateModel);
    }
}
