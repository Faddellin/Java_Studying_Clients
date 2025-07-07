package org.example.clientsbackend.Application.Producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.clientsbackend.Application.Models.Client.ClientPhoneUpdateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger("JSON_BUSINESS_LOGIC_LOGGER");

    public void sendClientPhoneUpdateMessage(String topicName, ClientPhoneUpdateModel messageObject) {
        try{
            kafkaTemplate.send(topicName, objectMapper.writeValueAsString(messageObject));
        }catch (Exception e){
            logger.error("Error while sending changeClientPhoneNumber message to Kafka", e);
        }
    }
}
