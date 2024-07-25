FROM openjdk:21-jdk-alpine
COPY target/starwars-cli-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
