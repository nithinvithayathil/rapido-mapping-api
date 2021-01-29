FROM openjdk:8-alpine
ADD target/rapido-mapping.jar rapido-mapping.jar
ENTRYPOINT ["java", "-jar", "/rapido-mapping.jar"]
EXPOSE 7504
