FROM maven:3.9.9-eclipse-temurin-17
WORKDIR /workspace

COPY pom.xml .

COPY src ./src
RUN mvn -q -DskipTests package

EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java -jar /workspace/target/*.jar"]
