# Multi-stage build for Spring Boot apps from https://spring.io/guides/topicals/spring-boot-docker/
# First build our source code with maven wrapper using JDK image
# (JDK 17 image size ~= 358 MB)
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

# Now copy .jar file from target folder from above build stage and paste in JRE image
# (JRE 17 image size ~= 170 MB)
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Docker build image & run it:
# docker build -t eda-kafka-audit-api .
# docker run --name="eda-kafka-audit-api" -d -p 8083:8083 --env="BOOTSTRAP_BROKERS=192.168.61.96:9092" --env="BUCKET_NAME=eda-kafka-prj-bckt" --env="AWS_ACCESS_KEY_ID=abhi" --env="AWS_SECRET_ACCESS_KEY=shek" --env="AWS_DEFAULT_REGION=ap-south-1" eda-kafka-audit-api
# http://localhost:8083/actuator/health