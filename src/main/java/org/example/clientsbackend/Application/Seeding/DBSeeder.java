package org.example.clientsbackend.Application.Seeding;

import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Domain.Entities.Address;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DBSeeder implements CommandLineRunner {

    private final ClientRepository _clientRepository;

    @Value("${spring.application.profile}")
    private String activeProfile;

    public DBSeeder(ClientRepository clientRepository) {
        _clientRepository = clientRepository;
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
                        .name("Иван Иванов")
                        .email("ivan@example.com")
                        .age(30)
                        .address(Address.builder().city("Москва").street("Ленина, 10").build())
                        .build(),
                Client.builder()
                        .name("Петр Петров")
                        .email("petr@example.com")
                        .age(25)
                        .address(Address.builder().city("Санкт-Петербург").street("Невский, 20").build())
                        .build()
        );

        _clientRepository.saveAll(baseClients);
        _clientRepository.flush();
    }
}
