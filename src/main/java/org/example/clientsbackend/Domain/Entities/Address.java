package org.example.clientsbackend.Domain.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "addresses")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String city;

    @NotNull
    private String street;

}
