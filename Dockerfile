# Use Maven to build the project
FROM maven:3.9.6-eclipse-temurin-17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Package the application
RUN mvn package

# Use a slim JDK image for the final runtime
FROM eclipse-temurin:17-jdk


# Set working directory
WORKDIR /app

# Copy the built jar from the previous image
COPY --from=build /app/target/*.jar app.jar

# Set the command to run the jar
CMD ["java", "-jar", "app.jar"]

