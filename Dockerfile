RUN mkdir -p /home/user/.local/tmp/buildkit-mount4054033649/target

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container (Make sure to replace 'your-app.jar' with your actual JAR file)

# Expose the port that your application runs on
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]