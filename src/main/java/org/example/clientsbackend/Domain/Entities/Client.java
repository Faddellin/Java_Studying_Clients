package org.example.clientsbackend.Domain.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "clients")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@SuperBuilder
@DiscriminatorValue("CLIENT")
public class Client
        extends User {

    private Integer phoneNumber;

    @NotNull
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

}
