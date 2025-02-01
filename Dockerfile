# # #CREDIT: https://stackoverflow.com/questions/61108021/gradle-and-docker-how-to-run-a-gradle-build-within-docker-container
# # #Build stage

# # FROM gradle:latest AS BUILD
# # WORKDIR /usr/app/
# # COPY . . 
# # RUN gradle build

# # # Package stage

# # FROM openjdk:latest
# # ENV JAR_NAME=app-1.0.jar
# # ENV APP_HOME=/usr/app
# # WORKDIR $APP_HOME
# # COPY --from=BUILD $APP_HOME .
# # EXPOSE 8080
# # ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME 
# # Use a base image that has JDK and Gradle installed
# Use a base image that includes JDK
# Use a base image with Wine and JDK
# Use the official Microsoft Windows Server Core image with JDK
# Use Ubuntu as base image with OpenJDK
# Use the official Gradle Docker image
# Use the official Gradle Docker image
FROM gradle:7.5-jdk17

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container (including gradlew and other files)
COPY . .

# Ensure the gradle wrapper is executable
RUN chmod +x gradlew

# Expose the port that your application will run on (replace 8080 with the appropriate port)
EXPOSE 8080

# Run the application with the gradlew wrapper using the run task
CMD ["./gradlew", "run", "--no-daemon"]