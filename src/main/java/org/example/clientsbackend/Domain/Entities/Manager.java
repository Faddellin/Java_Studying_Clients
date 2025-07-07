package org.example.clientsbackend.Domain.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "managers")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@SuperBuilder
@DiscriminatorValue("MANAGER")
public class Manager
        extends User {

    @NotNull
    private Integer phoneNumber;

}
