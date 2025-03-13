# Базовый образ с Java 17 и Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

# Рабочая директория
WORKDIR /app

# Копируем исходники и настройки Maven
COPY pom.xml .
COPY src ./src

# Собираем проект
RUN mvn clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre-alpine

# Рабочая директория
WORKDIR /app

# Копируем JAR-файл из стадии сборки
COPY --from=build /app/target/*.jar app.jar

# Открываем порт
EXPOSE 8082

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]