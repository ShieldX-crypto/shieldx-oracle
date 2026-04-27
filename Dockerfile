FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src/ src/
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS runtime

ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /app

RUN useradd -r -u 1001 -g root appuser

COPY --from=build /app/target/*.jar /app/app.jar

RUN chown -R 1001:0 /app
USER 1001

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
