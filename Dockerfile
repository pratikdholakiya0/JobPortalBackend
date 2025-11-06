FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src /app/src
RUN mvn clean package -DskipTests

<<<<<<< HEAD
=======
# 2. RUN Stage (Use a minimal JRE for the final image)
>>>>>>> 2a1fb5319a8d9ec3f69472981c1564a4aa225c85
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the JAR from the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar
<<<<<<< HEAD
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
=======
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
>>>>>>> 2a1fb5319a8d9ec3f69472981c1564a4aa225c85
