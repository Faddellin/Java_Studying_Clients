package org.example.clientsbackend.Application.Models.Client.Enums;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Common.PaginationModel;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
@Schema(description = "Client list model with pagination")
public class ClientPagedListModel {

    @NotNull
    private List<ClientModel> clientModels;

    @NotNull
    private PaginationModel paginationModel;

}
