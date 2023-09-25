FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar common-dto.jar
ENTRYPOINT ["java","-jar","/common-dto.jar"]