FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY ./pom.xml ./pom.xml
COPY ./healthTrackerApp/pom.xml ./healthTrackerApp/pom.xml
COPY ./healthTrackerApp/vitamin-server ./healthTrackerApp/vitamin-server
WORKDIR /app/healthTrackerApp/vitamin-server
RUN mvn clean package

# Remove unnecessary files from the build stage
RUN rm -rf /root/.m2

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/healthTrackerApp/vitamin-server/target/vitamin-server-1.0-SNAPSHOT.jar /app/your-application.jar
CMD ["java", "-jar", "your-application.jar"]