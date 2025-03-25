FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем JAR и ресурсы (включая миграции) в правильные места
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/src/main/resources/ /app/resources/

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]