# Stage 1: Build application with Maven and Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy POM and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal Java 21 Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built artifact from builder stage
COPY --from=build /app/target/*.jar app.jar

# Render supplies $PORT dynamically
ENV PORT=8080
EXPOSE ${PORT}

# Run with production profile
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -Dspring.profiles.active=prod -jar app.jar"]
