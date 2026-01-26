# Step 1: Build the JAR (using Maven)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first (faster builds later)
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Step 2: Runtime (The actual "container" that runs on the server)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# IMPORTANT: CoreNLP needs RAM.
# We limit the JVM to 3GB so it doesn't crash a 4GB server.
ENV JAVA_OPTS="-Xmx3g -Xss512k -XX:+UseSerialGC"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]