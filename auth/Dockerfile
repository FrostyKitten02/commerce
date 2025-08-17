# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/auth-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8000

# Set default environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]