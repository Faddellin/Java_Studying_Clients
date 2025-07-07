package org.example.clientsbackend.Presentation.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Common.ResponseModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ReportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportsController {

    private final ReportService _reportService;

    public ReportsController(ReportService reportService) {
        _reportService = reportService;
    }

    @Operation(
            summary = "Get report (For ADMIN only)",
            description = "Retrieve byte array of xlsx report about clients in system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Byte array of report retrieved",
                    content = {@Content(
                            mediaType = MediaType.ALL_VALUE,
                            schema = @Schema(type = "string", format = "binary")
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
    @GetMapping(path = "reports/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @SecurityRequirement(name = "JWT")
    public byte[] GetReport() throws ExceptionWrapper {
        return _reportService.getReport();
    }

}
