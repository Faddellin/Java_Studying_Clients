package org.example.clientsbackend.Application.Models.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientSortCriteria;

@AllArgsConstructor
@Getter @Setter
@Schema(description = "Indicates with what criteria and by what type of sorting to sort clients")
public class ClientSortModel {

    @NotNull
    private ClientSortCriteria sortCriteria;

    @NotNull
    private SortOrder sortOrder;

}
