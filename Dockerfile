# Use supported Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR file
COPY target/messge-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Start Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
