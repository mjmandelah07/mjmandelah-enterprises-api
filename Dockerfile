# Use the official Maven image to build the app
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvnw clean package

# Use a base image that supports Java 23
FROM openjdk:23-jdk

# Set the working directory
WORKDIR /app

# Copy the Spring Boot jar file to the container
COPY target/*.jar /app/app.jar

# Expose the port Spring Boot will run on
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
