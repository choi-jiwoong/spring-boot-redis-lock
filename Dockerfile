FROM amazoncorretto:20

COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
