# StarWars CLI

## Descripción
Esta herramienta CLI envía mensajes en formato Protobuf a un sistema de broker de mensajes (Kafka).

## Requisitos
- Java 21
- Apache Kafka
- Gradle

## Configuración

1. **Compilar el proyecto**

   ```bash
   ./gradlew build
    ```
2. **Crear archivo de configuración de Kafka**

   ```bash
    bootstrap.servers=localhost:9092
   ```
3.  **Preparar el archivo JSON**

   ```bash
    {
        "name": "Luke Skywalker",
        "id": 1,
        "email": "luke@force.com"
    } 
    ```
4.  **Ejecutar la herramienta**

   ```bash
    java -cp build/libs/message-broker-cli-1.0-SNAPSHOT.jar com.example.MessageBrokerCLI config.properties input.json
    ```


