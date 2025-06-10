package org.example.clientsbackend.Application.Models.Client;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientSortCriteria;

@Getter @Setter
public class ClientSortModel {

    @NotNull
    private ClientSortCriteria sortCriteria;

    @NotNull
    private SortOrder sortOrder;

}
