# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/catalog-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8001

# Set default environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8001

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]