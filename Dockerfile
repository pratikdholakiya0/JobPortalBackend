FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src /app/src
RUN mvn clean package -DskipTests

# 2. RUN Stage (Use a minimal JRE for the final image)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the JAR from the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
