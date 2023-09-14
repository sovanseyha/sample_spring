FROM openjdk:20-jdk

WORKDIR /app

COPY target/devops-test-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD java -jar devops-test-0.0.1-SNAPSHOT.jar