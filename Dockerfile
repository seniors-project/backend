FROM openjdk:17
ARG JAR_FILE=build/libs/seniors-0.0.1-SNAPSHOT.jar
COPY build/libs/seniors-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]