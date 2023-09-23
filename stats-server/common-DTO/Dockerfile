FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar common-DTO.jar
ENTRYPOINT ["java","-jar","/common-DTO.jar"]