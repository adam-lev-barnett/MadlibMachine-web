# Step 1: Build the JAR with Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source and build
COPY src ./src
RUN mvn package -DskipTests

# Step 2: Runtime with Java 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the JAR from the build stage
# The wildcard * ensures it finds the versioned JAR
COPY --from=build /app/target/madlibFile-machine-*.jar app.jar

# Memory settings for Stanford NLP
ENV JAVA_OPTS="-Xmx3g -Xms2g -XX:+UseSerialGC"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]