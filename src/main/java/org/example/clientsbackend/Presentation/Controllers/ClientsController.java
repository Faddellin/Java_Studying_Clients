package org.example.clientsbackend.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientEditModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientPagedListModel;
import org.example.clientsbackend.Application.Models.Common.ResponseModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Clients controller", description = "Allows to perform various actions with clients")
public class ClientsController {

    private final ClientService _clientService;

    public ClientsController(ClientService clientService) {
        _clientService = clientService;

    }

    @Operation(
            summary = "Delete client (For ADMIN, MANAGER only)",
            description = "Allows you to delete a client by his id"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Client deleted"
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
    @DeleteMapping(value = "clients/{id}")
    @SecurityRequirement(name = "JWT")
    public void DeleteClient(
            @PathVariable("id") @Parameter(description = "Client id", example = "523512", required = true) Long clientId
    ) throws ExceptionWrapper {
        _clientService.deleteClient(clientId);
    }

    @Operation(
            summary = "Update client (For ADMIN, MANAGER only)",
            description = "Allows you to update a client by his id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client updated"
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
    @PostMapping(path = "clients/{id}/update")
    @SecurityRequirement(name = "JWT")
    public void UpdateClient(
            @Valid @RequestBody ClientEditModel clientEditModel,
            @PathVariable("id") @Parameter(description = "Client id", example = "523512", required = true) Long clientId
    ) throws ExceptionWrapper {
        _clientService.updateClient(clientId, clientEditModel);
    }

    @Operation(
            summary = "Get clients (For ADMIN, MANAGER only)",
            description = "Allows you to get clients by filters and pagination with sorting"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ClientPagedListModel retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClientPagedListModel.class)
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
    @PostMapping(path = "clients/get-all")
    @SecurityRequirement(name = "JWT")
    public ClientPagedListModel GetClients(
            @Valid @RequestBody ClientFiltersModel clientFiltersModel
    ) {
        return _clientService.getClients(clientFiltersModel);
    }

    @Operation(
            summary = "Assign manager (For ADMIN, MANAGER only)",
            description = "Allows you to assign a specific manager to a specific client"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Manager assigned"
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
    @PostMapping(path = "clients/{clientId}/assign-manager/{managerId}")
    @SecurityRequirement(name = "JWT")
    public void AssignManagerToClient(
            @PathVariable @Parameter(description = "Client id", example = "523512", required = true) Long clientId,
            @PathVariable @Parameter(description = "Manager id", example = "412612", required = true) Long managerId
    ) throws ExceptionWrapper {
        _clientService.assignManagerToCleint(clientId, managerId);
    }

    @Operation(
            summary = "Get client by assigned manager id (For ADMIN, MANAGER only)",
            description = "Allows you to get all the clients assigned to a specific manager by manager id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Clients retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
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
    @GetMapping(path = "clients/by-manager/{managerId}")
    @SecurityRequirement(name = "JWT")
    public List<ClientModel> GetClientsByManagerId(
            @PathVariable @Parameter(description = "Manager id", example = "412612", required = true) Long managerId
    ) throws ExceptionWrapper {
        return _clientService.getClientsByManagerId(managerId);
    }
}
