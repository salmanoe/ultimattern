#
# BUILD STAGE
#
FROM maven:3.9.9-eclipse-temurin-21 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests -P dev

#
# PACKAGE STAGE
#
FROM openjdk:21
ARG JAR_FILE=/usr/src/app/target/security-*.jar
COPY --from=build ${JAR_FILE} security_app.jar
EXPOSE 8080
CMD ["java","-jar","security_app.jar"]
