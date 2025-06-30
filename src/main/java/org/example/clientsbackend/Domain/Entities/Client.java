package org.example.clientsbackend.Domain.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "clients")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Client {

    //Constructor for tests
    public Client(Long id, String name, String email, Integer age){
        this.id = id; this.name = name; this.email = email; this.age = age;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

}
