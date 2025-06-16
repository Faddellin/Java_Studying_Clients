package org.example.clientsbackend.Application.Models.Manager;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ManagerModel {

    @NotNull
    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private Integer phoneNumber;

}
