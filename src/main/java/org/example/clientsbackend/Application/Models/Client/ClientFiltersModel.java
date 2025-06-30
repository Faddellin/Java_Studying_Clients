package org.example.clientsbackend.Application.Models.Client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class ClientFiltersModel {

    @Min(1)
    @NotNull
    private Integer page;

    @Min(1)
    @NotNull
    private Integer size;

    @Valid
    private ClientSortModel sortType;

    @Valid
    private List<ClientFilterModel> filterModels;
}
