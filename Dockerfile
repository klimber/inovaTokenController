# syntax=docker/dockerfile:1
FROM amazoncorretto:18-alpine

# Set container user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set build directory
WORKDIR /app/src

# Copy source files
COPY --chown=spring:spring . .

# Build application jar
RUN ./gradlew -s -g .gradle --no-daemon --no-build-cache bootJar \
    && cp /app/src/build/libs/*SNAPSHOT.jar /app/inovaTokenController.jar \
    && rm -rf /app/src

# Set application directory
WORKDIR /app

# Set java opts
ENV JAVA_TOOL_OPTIONS="-Xmx128M"
ENV SPRING_PROFILES_ACTIVE="prod"

# Expose app ports
EXPOSE 8080

# Init app
ENTRYPOINT ["java", "-jar", "/app/inovaTokenController.jar"]
