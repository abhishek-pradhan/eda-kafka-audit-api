package com.mts.auditapi.service;

import com.mts.auditapi.events.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private SaveFile saveFile;

    @KafkaListener(topics = "${kafka.consumer.topic.city}", groupId = "${spring.kafka.consumer.group-id}-1")
    public void receiveMessage(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) String timestamp,
            @Header(KafkaHeaders.OFFSET) String offset,
            DomainEvent value) {
        try {
            logger.info(String.format("***** Audit-City consumed message: key=%s, topic=%s, timestamp=%s, offset=%s, value=%s *****", key, topic, timestamp, offset, value));

            String jsonResult = value.getDetailEntity().getJsonResult();
            this.saveFile.writeToS3("city_"+ key, jsonResult);
        }
        catch (Exception exception)
        {
            logger.error("Error occurred in receiveMessage():" + exception.toString());
        }
    }

    @KafkaListener(topics = "${kafka.consumer.topic.weather}", groupId = "${spring.kafka.consumer.group-id}-2")
    public void receiveMessageWeather(
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) String timestamp,
            @Header(KafkaHeaders.OFFSET) String offset,
            DomainEvent value) {
        try {
            logger.info(String.format("***** Audit-Weather consumed message: key=%s, topic=%s, timestamp=%s, offset=%s, value=%s *****", key, topic, timestamp, offset, value));

            String jsonResult = value.getDetailEntity().getJsonResult();
            this.saveFile.writeToS3("weather_"+ key, jsonResult);
        }
        catch (Exception exception)
        {
            logger.error("Error occurred in receiveMessageWeather():" + exception.toString());
        }
    }
}