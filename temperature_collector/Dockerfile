FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY src src
COPY pom.xml .
RUN mvn clean install
RUN ls target

FROM openjdk:21-jdk
#ENV BOOTSTRAP_SERVERS
COPY --from=build /build/target/temperature_collector-*.*-*.jar ./collector-with-deps.jar
ARG JAVA_OPTS
CMD java $JAVA_OPTS -jar collector-with-deps.jar