# Use a base image with JDK and Spring Boot support
FROM eclipse-temurin:24-jdk

# Set working directory
WORKDIR /app

# Copy the built jar (you’ll build it in Step 2)
COPY target/*.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
