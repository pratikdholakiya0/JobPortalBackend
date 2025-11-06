FROM eclipse-temurin:17-jre-jammy

# Using Eclipse Temurin JRE on Ubuntu 22.04 (jammy) is a reliable alternative
# if the standard openjdk images are causing connectivity issues.
# JRE is used for running, not building, which is more efficient.

EXPOSE 8080

# Set the working directory inside the container
WORKDIR /app

# Define the JAR file name argument.
ARG JAR_FILE=target/*.jar

# Copy the built JAR file into the container and rename it to the simple 'app.jar'
# This is where the file is created inside the container: /app/app.jar
COPY ${JAR_FILE} app.jar

# The ENTRYPOINT command is correct. It runs the consistently named 'app.jar'
# This command starts your Spring Boot application when the container launches.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]