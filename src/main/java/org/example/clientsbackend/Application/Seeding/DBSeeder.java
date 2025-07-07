package org.example.clientsbackend.Application.Seeding;

import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.AuthService;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {

    private final ClientRepository _clientRepository;
    private final AuthService _authService;


    @Value("${spring.application.profile}")
    private String activeProfile;

    public DBSeeder(
            ClientRepository clientRepository,
            AuthService authService
    ) {
        _clientRepository = clientRepository;
        _authService = authService;
    }

    @Override
    public void run(String... args){

        if(activeProfile.equals("test")) {
            return;
        }

        List<Client> clients = _clientRepository.findAll();

        if(!clients.isEmpty()){
            return;
        }

        List<Client> baseClients = List.of(
                Client.builder()
                        .username("Иван Иванов")
                        .email("ivan@example.com")
                        .age(30)
                        .address(Address.builder().city("Москва").street("Ленина, 10").build())
                        .phoneNumber(123456)
                        .passwordHash(_authService.getPasswordHash("123"))
                        .build(),
                Client.builder()
                        .username("Петр Петров")
                        .email("petr@example.com")
                        .age(25)
                        .address(Address.builder().city("Санкт-Петербург").street("Невский, 20").build())
                        .phoneNumber(321654)
                        .passwordHash(_authService.getPasswordHash("321"))
                        .build()
        );

        _clientRepository.saveAll(baseClients);
        _clientRepository.flush();
    }
}
