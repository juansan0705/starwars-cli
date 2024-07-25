package org.mb.tech;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mb.tech.model.PersonJson;
import org.mb.tech.producer.PersonProducer;
import org.mb.tech.proto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;

@Component
@Command(name = "send-person", mixinStandardHelpOptions = true, version = "1.0",
        description = "Sends a Person protobuf message to Kafka")
public class CLICommand implements CommandLineRunner, Runnable {

    @Autowired
    private PersonProducer personProducer;

    @Option(names = {"-f", "--file"}, required = true, description = "JSON file containing person data")
    private File jsonFile;

    public static void main(String[] args) {
        new CommandLine(new CLICommand()).execute(args);
    }

    @Override
    public void run(String... args) {
        CommandLine.run(this, args);
    }

    @Override
    public void run() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PersonJson personJson = objectMapper.readValue(jsonFile, PersonJson.class);
            Person person = Person.newBuilder()
                    .setName(personJson.getName())
                    .setId(personJson.getId())
                    .setEmail(personJson.getEmail())
                    .build();
            personProducer.sendMessage(person);
            System.out.println("Message sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
