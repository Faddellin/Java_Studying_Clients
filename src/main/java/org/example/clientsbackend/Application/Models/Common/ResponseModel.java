package org.example.clientsbackend.Application.Models.Common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Dictionary;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
@Schema(description = "Error response model")
public class ResponseModel {

    @NotNull
    @Schema(description = "Error status code", example = "400")
    private Integer statusCode;

    @NotNull
    @Schema(description = "Error status code", example = "{\"userCreateModel\": \"User with such email or username already exists\"}")
    private Dictionary<String, String> errors;
}
