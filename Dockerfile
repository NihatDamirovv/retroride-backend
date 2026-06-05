# Use Java 21 runtime
FROM eclipse-temurin:26-jdk

# App directory
WORKDIR /app

# Copy jar file
COPY retroride/build/libs/*.jar app.jar

# Create uploads directory
RUN mkdir -p uploads

# Expose Spring Boot port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]