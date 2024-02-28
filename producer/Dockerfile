FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY src src
COPY pom.xml .
RUN mvn clean install

FROM openjdk:21-jdk
#ENV BOOTSTRAP_SERVERS
COPY --from=build /build/target/producer-*.*-*-jar-with-dependencies.jar ./producer-with-deps.jar
ARG JAVA_OPTS
CMD java $JAVA_OPTS -jar producer-with-deps.jar