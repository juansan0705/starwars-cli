package org.mb.tech.producer;

import org.mb.tech.proto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PersonProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    public PersonProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Person person) {
        kafkaTemplate.send("persons-topic", person.toByteArray());
    }
}
