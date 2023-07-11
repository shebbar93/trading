# Use a base image with Java and Maven installed
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project definition files to the container
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline -B

# Copy the application source code to the container
COPY src ./src

# Build the application JAR file
RUN mvn package -DskipTests

# Use a base image with Java installed
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the container
COPY --from=build /app/target/trading-*.jar app.jar
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

EXPOSE 8080
ENTRYPOINT ["./wait-for-it.sh", "mysql:3307", "--", "java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
