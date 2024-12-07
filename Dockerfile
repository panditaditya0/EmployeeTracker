FROM amazoncorretto:17-alpine-jdk
MAINTAINER baeldung.com
COPY target/psl-user-heartbeat.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]