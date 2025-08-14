# Use an official OpenJDK 21 image from Eclipse Temurin
FROM eclipse-temurin:21-jdk as base

# Set a working directory inside the container
WORKDIR /app

ARG JAR_FILE=target/*.jar
# Copy the jar file built by Maven/Gradle into the container
COPY ${JAR_FILE} serviceregistry.jar

# Expose Eureka's default port
EXPOSE 8761

# Start the Eureka server
ENTRYPOINT ["java", "-jar", "serviceregistry.jar"]
