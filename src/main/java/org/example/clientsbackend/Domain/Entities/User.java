package org.example.clientsbackend.Domain.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.clientsbackend.Domain.Enums.UserRole;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_role")
@DiscriminatorValue("ADMIN")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Column(unique = true)
    protected String email;

    @NotNull
    @Column(unique = true)
    protected String username;

    @NotNull
    protected String passwordHash;

    @Column(name = "user_role", insertable=false, updatable=false)
    @Enumerated(EnumType.STRING)
    protected UserRole userRole;

}
