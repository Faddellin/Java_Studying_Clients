package org.example.clientsbackend.Application.Models.Client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Validators.ClientFilter.ClientFilterConstraint;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class ClientFiltersModel {

    @Min(1)
    @NotNull
    private Integer page = 1;

    @Min(1)
    @NotNull
    private Integer size = 1;

    private ClientSortModel sortType;

    @Valid
    private List<ClientFilterModel> filterModels;
}
