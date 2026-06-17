
# Multi-stage Dockerfile for the url-shortener Spring Boot application
#  - Build stage uses Maven with JDK 17 to create the fat jar
#  - Final stage runs the jar on a lightweight JRE image

### Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy only the pom first to leverage Docker layer caching for dependencies
COPY pom.xml ./
# Copy the source code
COPY src ./src

# Build the application (skip tests for faster builds in CI; remove -DskipTests for full verification)
RUN mvn -B -DskipTests package

### Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built jar from the build stage. Adjust the jar name if artifactId/version is changed in pom.xml
COPY --from=build /workspace/target/url-shortener-0.0.1-SNAPSHOT.jar ./app.jar

# Expose default Spring Boot port (change if the app uses a different port)
EXPOSE 8080

# Use a small heap by default; you can tune these flags as needed
ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","/app/app.jar"]


