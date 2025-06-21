# Multi-stage build for Spring Boot application
FROM maven:3.9.5 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-slim

WORKDIR /app

# Install curl and postgresql-client for health checks and wait script
RUN apt-get update && apt-get install -y --no-install-recommends curl postgresql-client && apt-get clean && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r trading && useradd -r -g trading trading

# Copy jar from build stage
COPY --from=build /app/target/trading-sandbox-1.0.0.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && chown -R trading:trading /app

# Copy wait-for-postgres.sh script
COPY wait-for-postgres.sh /wait-for-postgres.sh
RUN chmod +x /wait-for-postgres.sh

# Switch to non-root user
USER trading

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Run the application
ENTRYPOINT ["/wait-for-postgres.sh", "postgres", "java", "-jar", "app.jar"] 