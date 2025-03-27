# Стадия 1: Сборка common-events и основного сервиса
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# --- Сборка common-events ---
COPY common-events/pom.xml ./common-events/pom.xml
COPY common-events/src ./common-events/src
RUN mvn -f ./common-events/pom.xml clean install -DskipTests

# --- Сборка order-ride-service ---
COPY order-ride-service/pom.xml ./order-ride-service/pom.xml
COPY order-ride-service/src ./order-ride-service/src
RUN mvn -f ./order-ride-service/pom.xml clean package -DskipTests

# Стадия 2: Финальный образ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Копируем JAR из target папки order-ride-service
COPY --from=build /app/order-ride-service/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
