# Image: Builder (temp)
FROM openjdk:8-jdk-alpine AS builder
WORKDIR /src/

# Copy source code and build
COPY gradle gradlew settings.gradle.kts build.gradle.kts gradle.properties src /src/
RUN ./gradlew build

#Image: final
FROM openjdk:8-jre-alpine
ENV MICRONAUT_SERVER_PORT 80

WORKDIR /app

RUN apk add --no-cache curl
COPY --from=builder /src/build/libs/*-all.jar /app/microservice.jar

EXPOSE 80

ENV LOG_APPENDER classic-stdout

HEALTHCHECK --interval=5m --timeout=5s --retries=3 --start-period=1m CMD curl --fail http://localhost/health || exit 1

CMD ["java", \
     "-XX:+UnlockExperimentalVMOptions", \
     "-XX:+UseCGroupMemoryLimitForHeap", \
     "-noverify", \
     "-XX:TieredStopAtLevel=1", \
     "-Dcom.sun.management.jmxremote", \
     "-jar", "/app/microservice.jar"]