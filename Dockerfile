# Build Stage
FROM maven:3.8.5-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/psl-user-heartbeat.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
