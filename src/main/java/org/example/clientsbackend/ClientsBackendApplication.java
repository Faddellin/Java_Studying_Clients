package org.example.clientsbackend;

import org.example.clientsbackend.Application.Repositories.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = "org.example.clientsbackend.Application.Repositories",
        repositoryBaseClass = BaseRepositoryImpl.class
)
@SpringBootApplication
public class ClientsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientsBackendApplication.class, args);
    }

}
