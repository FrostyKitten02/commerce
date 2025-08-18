# Use OpenJDK 21 as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/checkout-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8004

# Set default environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8004

# Create directory for external config
RUN mkdir -p /app/config

# Run the application with support for external config
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=optional:file:/app/config/"]