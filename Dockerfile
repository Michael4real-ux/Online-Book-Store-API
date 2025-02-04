# Use the official Maven image to build the application
FROM maven:3.8.1-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/bookstore-api-0.0.1-SNAPSHOT.jar bookstore-api.jar
ENTRYPOINT ["java", "-jar", "/bookstore-api.jar"]