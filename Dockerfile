# ---------- Build stage ----------
FROM gradle:8.4.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Copiamos SOLO el jar final al contenedor
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
