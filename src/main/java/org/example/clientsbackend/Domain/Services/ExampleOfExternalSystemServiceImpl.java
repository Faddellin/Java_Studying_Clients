package org.example.clientsbackend.Domain.Services;

import lombok.AllArgsConstructor;
import org.example.clientsbackend.Application.Models.Client.ClientPhoneUpdateModel;
import org.example.clientsbackend.Application.Producers.DefaultKafkaProducer;
import org.example.clientsbackend.Application.ServicesInterfaces.ExampleOfExternalSystemService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExampleOfExternalSystemServiceImpl
        implements ExampleOfExternalSystemService {

    private String clientChangedPhoneTopic;

    private final DefaultKafkaProducer kafkaProducer;

    public void changeClientPhoneNumber(Integer newPhoneNumber, Long clientId){
        kafkaProducer.sendClientPhoneUpdateMessage(clientChangedPhoneTopic, new ClientPhoneUpdateModel(clientId, newPhoneNumber));
    }
}
