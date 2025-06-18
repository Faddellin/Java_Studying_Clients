package org.example.clientsbackend.Application.Models.Manager;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ManagerModel {

    @NotNull
    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private Integer phoneNumber;

}
