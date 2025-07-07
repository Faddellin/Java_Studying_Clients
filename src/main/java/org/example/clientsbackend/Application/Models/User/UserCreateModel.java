package org.example.clientsbackend.Application.Models.User;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerCreateModel;
import org.example.clientsbackend.Domain.Enums.UserRole;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "userRole"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientCreateModel.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = ManagerCreateModel.class, name = "MANAGER"),
        @JsonSubTypes.Type(value = AdminCreateModel.class, name = "ADMIN"),
})
@Data
@Schema(description = "User create model")
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class UserCreateModel {

    @NotNull
    @Schema(description = "User username", example = "exampleUsername")
    private String username;

    @NotNull
    @Email
    @Schema(description = "User email", example = "exampleEmail@mail.ru")
    private String email;

    @NotNull
    @Schema(description = "User password", example = "superSecretPassword123")
    private String password;

    @JsonTypeId
    @Schema(description = "User role", example = "CLIENT")
    private UserRole userRole;

}
