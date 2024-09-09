FROM eclipse-temurin:17-jdk-alpine
LABEL author="Muhammad"
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD ["java", "-jar", "/app.jar"]