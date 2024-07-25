package org.mb.tech;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mb.tech.model.PersonJson;
import org.mb.tech.producer.PersonProducer;
import org.mb.tech.proto.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@SpringBootApplication
public class CLICommand implements Callable<Integer> {

    @Autowired
    private PersonProducer personProducer;

    @Option(names = {"-f", "--file"}, required = true, description = "JSON file containing person data")
    private File jsonFile;

    public static void main(String[] args) {
        // Initialize Spring Boot context
        SpringApplication app = new SpringApplication(CLICommand.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        app.run(args);

        // After Spring Boot initialization, run the Picocli command
        CommandLine cmd = new CommandLine(new CLICommand());
        int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
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
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
