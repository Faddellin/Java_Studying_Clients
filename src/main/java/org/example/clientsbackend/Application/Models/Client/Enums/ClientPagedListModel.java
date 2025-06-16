package org.example.clientsbackend.Application.Models.Client.Enums;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Client.ClientModel;
import org.example.clientsbackend.Application.Models.Common.PaginationModel;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Manager;

import java.util.List;

@AllArgsConstructor
@Getter @Setter
public class ClientPagedListModel {

    @NotNull
    private List<ClientModel> clientModels;

    @NotNull
    private PaginationModel paginationModel;

}
