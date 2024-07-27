# Message Broker CLI

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
3.  **Set up Kafka Broker**

- First step, download the Kafka distribution from the official Apache Kafka website.
   
- Second step, unzip the file preferably in C:\ and open a command prompt and navigate to the Kafka directory.
   
- Third step, run the Zookeeper server.
  ```
  .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
  ``` 
- Fourth step, run the Kafka server.
  ```
  .\bin\windows\kafka-server-start.bat .\config\server.properties
  ``` 
- Fifth step, create a topic.
  ```
   .\bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic person-topic
  ```
- Sixth step, run the consumer.
  ```
  .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic person-topic --from-beginning
  ```
4.  **Prepare the JSON file to send to the consumer (this file is in src/main/resources/person.json)**

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

- And finally check in your consumer that it has received the message (json) correctly

![img.png](img.png)
