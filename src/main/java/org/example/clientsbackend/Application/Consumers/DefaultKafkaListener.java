package org.example.clientsbackend.Application.Consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Client.ClientPhoneUpdateModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultKafkaListener {

    private final Logger businessLogiclogger = LoggerFactory.getLogger("JSON_BUSINESS_LOGIC_LOGGER");
    private final Logger errorsLogger = LoggerFactory.getLogger("JSON_ERRORS_LOGGER");
    private final ClientService _clientService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topics.client-changed-phone-topic}", groupId = "group-1")
    void clientChangerPhoneTopicListener(String clientPhoneUpdateModelSerialized) {
        try{
            ClientPhoneUpdateModel clientPhoneUpdateModel = objectMapper.readValue(clientPhoneUpdateModelSerialized, ClientPhoneUpdateModel.class);
            _clientService.updateClientPhoneNumber(clientPhoneUpdateModel);
            businessLogiclogger.info("User ({}) phone was updated", clientPhoneUpdateModel.getClientId());
        }catch (ExceptionWrapper e){
            errorsLogger.error("Error during kafka message processing. Errors: {}", e.getErrors(), e);
        } catch (Exception e) {
            errorsLogger.error("Error during kafka message processing", e);
        }
    }
}
