package org.example.clientsbackend.Application.Models.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
@Schema(description = "Filters model for users")
public class ClientFiltersModel {

    @Min(1)
    @NotNull
    @Schema(description = "New client username", example = "1")
    private Integer page;

    @Min(1)
    @NotNull
    @Schema(description = "New client username", example = "5")
    private Integer size;

    @Valid
    private ClientSortModel sortType;

    @Valid
    private List<ClientFilterModel> filterModels;
}
