package org.mb.tech;

import com.fasterxml.jackson.databind.JsonNode;
import org.mb.tech.proto.Person;
import org.mb.tech.service.impl.JsonServiceImpl;
import org.mb.tech.service.impl.KafkaServiceImpl;
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
        try (KafkaServiceImpl kafkaServiceImpl = new KafkaServiceImpl(props)) {
            JsonServiceImpl jsonServiceImpl = new JsonServiceImpl();
            JsonNode jsonNode = jsonServiceImpl.readJsonFile(jsonFile);
            Person person = createProtobufPerson(jsonNode);
            byte[] messageBytes = person.toByteArray();
            kafkaServiceImpl.sendKafkaMessage(messageBytes);
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

    private Person createProtobufPerson(JsonNode jsonNode) {
        return Person.newBuilder()
                .setName(jsonNode.get("name").asText())
                .setId(jsonNode.get("id").asInt())
                .setEmail(jsonNode.get("email").asText())
                .build();
    }
}