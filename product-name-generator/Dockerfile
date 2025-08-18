FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/product-name-generator-*.jar app.jar

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=optional:file:/app/config/"]