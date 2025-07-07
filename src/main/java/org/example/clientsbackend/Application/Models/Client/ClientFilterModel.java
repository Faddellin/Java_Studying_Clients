package org.example.clientsbackend.Application.Models.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Validators.ClientFilter.ClientFilterConstraint;

@ClientFilterConstraint
@AllArgsConstructor
@Getter @Setter
@Schema(description = "Stores values for sorting and filtering the user")
public class ClientFilterModel {

    @NotNull
    private ClientFilterCriteria filterCriteria;

    @NotNull
    private FilterOperator filterOperator;

    @NotNull
    @Schema(description = "Value by which the sorting will take place", example = "exampleEmail@mail.ru")
    private Object value;

}
