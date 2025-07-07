package org.example.clientsbackend.Application.Models.Manager;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Schema(description = "Manager model")
public class ManagerModel {

    @NotNull
    @Schema(description = "Manager id", example = "4312")
    private Long id;

    @NotNull
    @Schema(description = "Manager username", example = "AndreyManager1999")
    private String username;

    @NotNull
    @Schema(description = "Manager email", example = "email@mail.ru")
    private String email;

    @NotNull
    @Schema(description = "Manager phone number", example = "8950123")
    private Integer phoneNumber;

}
