package org.example.clientsbackend.Application.Models.Client;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ClientPhoneUpdateModel {

    @NotNull
    private Long clientId;

    @NotNull
    private Integer phoneNumber;
}
