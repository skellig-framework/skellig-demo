FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENV SKELLIG_PROFILE="local"
ENTRYPOINT java -Dtest.profile=$SKELLIG_PROFILE -jar /app.jar
