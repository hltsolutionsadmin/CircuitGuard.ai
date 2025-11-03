# Multi-stage build: builder uses Maven with Temurin 21, runtime uses Temurin 21

# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy entire mono-repo so Maven can resolve parent POM and sibling modules (e.g., commonservices)
COPY . .

# Build only the usermanagement module and its dependencies, skipping tests for speed
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl usermanagement -am -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /workspace/usermanagement/target/usermanagement.jar /app/app.jar

# Expose service port
EXPOSE 9010

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
