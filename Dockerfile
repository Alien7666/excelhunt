FROM openjdk:17-oracle
WORKDIR /app
ADD target/Excelhunt.jar /app/Excelhunt.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/Excelhunt.jar"]
