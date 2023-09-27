FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-src.jar
ENTRYPOINT ["java","-jar","/stats-src.jar"]