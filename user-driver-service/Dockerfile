FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# --- Сборка common-events ---
COPY common-events/pom.xml ./common-events/pom.xml
COPY common-events/src ./common-events/src
RUN mvn -f ./common-events/pom.xml clean install -DskipTests

# --- Сборка user-driver-service ---
# Используем правильное имя папки!
COPY user-driver-service/pom.xml ./user-driver-service/pom.xml
COPY user-driver-service/src ./user-driver-service/src

RUN mvn -f ./user-driver-service/pom.xml clean package -DskipTests

# Стадия 2: Финальный образ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Копируем JAR из target папки user-driver-service
COPY --from=build /app/user-driver-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
