FROM amazoncorretto:11-alpine
ADD build/libs/votingsession-0.0.1-SNAPSHOT.jar /
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "votingsession-0.0.1-SNAPSHOT.jar"]
