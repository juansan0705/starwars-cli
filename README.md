# StarWars CLI

## Description

This is a guide to using the CLI tool that sends events in Protobuf format to a message broker system (Kafka).

## Requirements

- Java 21
- Apache Kafka
- Protobuf
- Maven

## Configuration

1. **Compile the project**

   ```bash
    mvn clean compile
    ```
2. **Package the project**

   ```bash
    mvn clean package
    ```
3.  **Create Kafka Broker**

   ```bash
- First step, download the Kafka file from the official Apache Kafka website.
   
- Second step, unzip the file preferably in C:\ and open the cmd terminal.
   
- Third step, run the Zookeeper server.
  ```
  .\bin\windows\zookeeper-server-start.bat .\config\zookeeper
  ``` 
- Fourth step, run the Kafka server.
  ```
  .\bin\windows\kafka-server-start.bat .\config\server.properties
  ``` 
- Fifth step, create a topic.
  ```
   kafka-topics.bat --create --bootstrap-server localhost:9092 --topic test
  ```
- Sixth step, run the producer and consumer.
  ```
  kafka-console-producer.bat --broker-list localhost:9092 --topic test
  ```
4.  **Prepare the JSON file to send to the consumer**

```bash
 {
     "name": "Luke Skywalker",
     "id": 1,
     "email": "luke@force.com"
 } 
 ```

5. **Run the message broker from the terminal**

```bash
 java -jar target/starwars-cli-1.0-SNAPSHOT-jar-with-dependencies.jar "localhost:9092" "src/main/resources/person.json"
```


