package org.example.clientsbackend.Application.Models.Client;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientSortCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.example.clientsbackend.Application.Validators.ClientFilter.ClientFilterConstraint;

@ClientFilterConstraint
@AllArgsConstructor
@Getter @Setter
public class ClientFilterModel {

    @NotNull
    private ClientFilterCriteria filterCriteria;

    @NotNull
    private FilterOperator filterOperator;

    @NotNull
    private Object value;

}
