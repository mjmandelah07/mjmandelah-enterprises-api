# Use a Maven image that includes OpenJDK 21 for building the app
FROM maven:4.0.0-openjdk-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY . .

# Build the application, skipping tests for faster builds
RUN mvn clean package -DskipTests

# Use the OpenJDK 21 image for running the app
FROM openjdk:21-jdk-alpine

# Set the working directory for the running container
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage to the running stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
