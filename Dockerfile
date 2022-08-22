# Defining docker image
FROM openjdk:17
LABEL maintainer="madridproject.net"
ADD target/demo-project-0.0.1-SNAPSHOT.jar my-spring-project-demo.jar
#       - copying the jar file to docker image
ENTRYPOINT ["java", "-jar", "my-spring-project-demo.jar"]