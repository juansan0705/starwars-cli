package org.mb.tech;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.mb.tech.proto.Person;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import java.io.File;
import java.util.Properties;
import java.util.concurrent.Callable;

@Command(name = "CLICommand", mixinStandardHelpOptions = true, version = "1.0",
        description = "Sends protobuf messages to a Kafka broker.")
public class CLICommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The Kafka broker address.")
    public String broker;

    @Parameters(index = "1", description = "The JSON file containing the person data.")
    public File jsonFile;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CLICommand()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        // Configure Kafka producer
        Properties props = new Properties();
        props.put("bootstrap.servers", broker);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);

        // Read JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonFile);

        // Create Protobuf object from JSON
        Person person = Person.newBuilder()
                .setId(jsonNode.get("id").asInt())
                .setName(jsonNode.get("name").asText())
                .setEmail(jsonNode.get("email").asText())
                .build();

        // Convert protobuf message to byte array
        byte[] messageBytes = person.toByteArray();

        // Send message to Kafka
        ProducerRecord<String, byte[]> record = new ProducerRecord<>("person-topic", messageBytes);
        producer.send(record);

        producer.close();
        System.out.println("Message sent successfully");

        return 0;
    }
}