# Stage 1: Build the application
FROM eclipse-temurin:23 AS builder

# Install dependencies and Gradle manually
RUN apt-get update && apt-get install -y wget unzip

# Set the Gradle version as an argument for easy updates
ARG GRADLE_VERSION=8.2.1
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp \
    && unzip /tmp/gradle-${GRADLE_VERSION}-bin.zip -d /opt \
    && rm /tmp/gradle-${GRADLE_VERSION}-bin.zip

# Add Gradle to PATH
ENV GRADLE_HOME=/opt/gradle-${GRADLE_VERSION}
ENV PATH="${GRADLE_HOME}/bin:${PATH}"

# Set the working directory for the application
WORKDIR /app

# Copy Gradle wrapper and project files to leverage Docker layer caching
COPY build.gradle settings.gradle gradlew* ./
COPY gradle ./gradle

# Download dependencies (cache layer)
RUN gradle dependencies --no-daemon

# Copy the source code and build the application
COPY src ./src
RUN gradle bootJar --no-daemon

# Stage 2: Create a minimal production image
FROM eclipse-temurin:23-jre

# Set environment variables for production
ENV JAVA_OPTS="-Xms512m -Xmx1024m" \
    APP_HOME=/app

# Set the working directory
WORKDIR $APP_HOME

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
