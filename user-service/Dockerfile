FROM openjdk:21-jdk-slim

LABEL maintainer="Women Safety Platform Team"
LABEL description="User Service for Women Safety Platform"

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Create final layer with just the JAR
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=0 /app/target/user-service-1.0.0.jar app.jar

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]