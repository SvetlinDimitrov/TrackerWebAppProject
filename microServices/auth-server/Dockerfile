# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /workspace
COPY pom.xml .
COPY src src
RUN mvn clean package

# Run stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /workspace/target/auth-server-1.0-SNAPSHOT.jar /app/your-application.jar
CMD ["java", "-jar", "your-application.jar"]