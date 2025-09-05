# ---- Stage 1: Build and test the application ----
FROM maven:3.9.11-eclipse-temurin-24

WORKDIR /src

# Copy the source code
COPY . .

# Download dependencies
RUN mvn dependency:go-offline

# Test the application
ENTRYPOINT [ "mvn", "clean", "verify" ]