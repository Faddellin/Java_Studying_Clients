package org.example.clientsbackend.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.clientsbackend.Application.Models.Common.ResponseModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagersController {

    private final ManagerService _managerService;

    public ManagersController(ManagerService managerService) {
        _managerService = managerService;
    }

    @Operation(
            summary = "Get managers (For ADMIN only)",
            description = "Retrieve you all managers in system"
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
    @GetMapping(path = "managers")
    @SecurityRequirement(name = "JWT")
    public List<ManagerModel> GetManagers() {
        return _managerService.getManagers();
    }
}
