FROM eclipse-temurin:24-jdk
EXPOSE 8080
WORKDIR .
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]