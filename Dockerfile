# Base image with Maven + Java 21
FROM maven:3.9.9-eclipse-temurin-21

# Working directory inside container
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download all dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy project source code
COPY src ./src

# Create necessary directories for reports
RUN mkdir -p allure-results target

# Set environment variable to enable CI mode
ENV CI=true

# Default command for local container execution
CMD ["mvn", "clean", "test"]
