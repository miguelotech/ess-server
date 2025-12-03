# Etapa 1: Construcción (Build)
FROM gradle:jdk17-alpine AS builder
WORKDIR /app
COPY . .
# Construir el proyecto saltando los tests para agilizar el deploy (opcional, quitar -x test si quieres tests)
RUN ./gradlew build -x test --no-daemon

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copiar el jar generado desde la etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]