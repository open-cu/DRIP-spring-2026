FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/openapi-codegen-demo-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /workspace/target/generated-sources /app/generated-sources
ENV CODEGEN_GENERATED_DIR=/app/generated-sources
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
