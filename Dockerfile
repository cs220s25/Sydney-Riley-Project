# Use Maven to build the project
FROM amazonlinux

# Set the working directory inside the container
RUN yum install -y maven-amazon-corretto21
WORKDIR /app

# Copy pom.xml and install dependencies
COPY . .

# Package the application
RUN mvn package

# Set the command to run the jar
CMD ["java", "-jar", "target/dbot-1.0-SNAPSHOT-jar-with-dependencies.jar"]

