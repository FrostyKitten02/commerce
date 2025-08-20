FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/health-monitor-*.jar app.jar

EXPOSE 8086

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=optional:file:/app/config/"]