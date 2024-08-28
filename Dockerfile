# syntax=docker/dockerfile:1

FROM maven:3.8.5-openjdk-17

WORKDIR /app

COPY .mvn/ .mvn

COPY mvnw pom.xml ./

RUN mvn clean install -U

COPY src ./src

CMD ["./mvn" "spring-boot:run"] 