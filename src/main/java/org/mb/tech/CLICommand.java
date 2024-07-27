package org.mb.tech;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.mb.tech.proto.Person;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@Command(name = "CLICommand", mixinStandardHelpOptions = true, version = "1.0",
        description = "Sends protobuf messages to a Kafka broker.")
public class CLICommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The Kafka broker address.")
    private String broker;

    @Parameters(index = "1", description = "The JSON file containing the person data.")
    private File jsonFile;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CLICommand()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        Properties props = createKafkaProperties();
        try (KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props)) {
            JsonNode jsonNode = readJsonFile(jsonFile);
            Person person = createProtobufPerson(jsonNode);
            byte[] messageBytes = person.toByteArray();
            sendKafkaMessage(producer, messageBytes);
            System.out.println("Message sent successfully");
        } catch (IOException | ExecutionException | InterruptedException e) {
            System.err.println("Error sending message: " + e.getMessage());
            return 1;
        }
        return 0;
    }

    private Properties createKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        return props;
    }

    private JsonNode readJsonFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(file);
    }

    private Person createProtobufPerson(JsonNode jsonNode) {
        return Person.newBuilder()
                .setName(jsonNode.get("name").asText())
                .setId(jsonNode.get("id").asInt())
                .setEmail(jsonNode.get("email").asText())
                .build();
    }

    private void sendKafkaMessage(KafkaProducer<String, byte[]> producer, byte[] messageBytes) throws ExecutionException, InterruptedException {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>("person-topic", messageBytes);
        RecordMetadata metadata = producer.send(record).get();
        System.out.printf("Message sent to topic %s partition %d offset %d%n", metadata.topic(), metadata.partition(), metadata.offset());
    }
}
