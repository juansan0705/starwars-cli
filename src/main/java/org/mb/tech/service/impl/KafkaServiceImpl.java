package org.mb.tech.service.impl;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.mb.tech.service.KafkaService;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaServiceImpl implements KafkaService{

    private final KafkaProducer<String, byte[]> producer;

    public KafkaServiceImpl(Properties props) {
        this.producer = new KafkaProducer<>(props);
    }
    @Override
    public void sendKafkaMessage(byte[] messageBytes) throws ExecutionException, InterruptedException {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>("person-topic", messageBytes);
        RecordMetadata metadata = producer.send(record).get();
        System.out.printf("Message sent to topic %s partition %d offset %d%n", metadata.topic(), metadata.partition(), metadata.offset());
    }

    public void close() {
        producer.close();
    }
}