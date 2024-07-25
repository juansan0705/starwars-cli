# StarWars CLI

## Descripción

Esta es una guía para usar la herramienta CLI que envía eventos en formato Protobuf a un sistema de broker de mensajes (Kafka).

## Requisitos

- Java 21
- Apache Kafka
- Protobuf
- Maven

## Configuración

1. **Compilar el proyecto**

   ```bash
    mvn clean compile
    ```
2. **Empaquetar el proyecto**

   ```bash
    mvn clean package
    ```
3.  **Crear Broker de Kafka**

   ```bash
- Primer paso descargar el archivo de Kafka desde la página oficial de Apache Kafka.
   
- Segundo paso descomprimir el archivo preferiblemente en C:\ y abrir la terminal de comandos.
   
- Tercer paso ejecutar el servidor de Zookeeper.
  ```
  .\bin\windows\zookeeper-server-start.bat .\config\zookeeper
  ``` 
- Cuarto paso ejecutar el servidor de Kafka.
  ```
  .\bin\windows\kafka-server-start.bat .\config\server.properties
  ``` 
- Quinto paso crear un tópico.
  ```
   kafka-topics.bat --create --bootstrap-server localhost:9092 --topic test
  ```
- Sexto paso ejecutar el productor y consumidor.
  ```
  kafka-console-producer.bat --broker-list localhost:9092 --topic test
  ```
4.  **Preparar el archivo JSON para mandarlo al consumidor**

```bash
 {
     "name": "Luke Skywalker",
     "id": 1,
     "email": "luke@force.com"
 } 
 ```

5. **Ejecutar el broker de mensajes desde la terminal**

```bash
 java -jar target/starwars-cli-1.0-SNAPSHOT-jar-with-dependencies.jar "localhost:9092" "src/main/resources/person.json"
```


