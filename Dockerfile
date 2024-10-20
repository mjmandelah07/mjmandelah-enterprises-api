# Use an Amazon Corretto Maven image for building the app
FROM amazoncorretto:21 AS build

# Install Maven
RUN apt-get update && \
    apt-get install -y maven

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY . .

# Build the application, skipping tests for faster builds
RUN mvn clean package -DskipTests

# Use the Amazon Corretto 21 image for running the app
FROM amazoncorretto:21

# Set the working directory for the running container
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage to the running stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
