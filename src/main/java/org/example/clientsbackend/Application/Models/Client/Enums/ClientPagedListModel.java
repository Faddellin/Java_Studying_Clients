package org.example.clientsbackend.Application.Models.Client.Enums;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Common.PaginationModel;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class ClientPagedListModel {

    @NotNull
    private List<ClientModel> clientModels;

    @NotNull
    private PaginationModel paginationModel;

}
