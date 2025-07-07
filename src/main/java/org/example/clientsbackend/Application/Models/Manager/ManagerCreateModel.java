package org.example.clientsbackend.Application.Models.Manager;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.clientsbackend.Application.Models.User.UserCreateModel;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Schema(description = "Manager create model")
public class ManagerCreateModel
    extends UserCreateModel {

    @NotNull
    @Schema(description = "Manager phone number", example = "95235235")
    private Integer phoneNumber;

}
