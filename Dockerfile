# ===== BUILD STAGE =====
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copiamos solo lo necesario para resolver dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copiamos el código fuente
COPY src src

# Construimos el JAR
RUN ./mvnw clean package -DskipTests


# ===== RUN STAGE =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos SOLO el JAR final
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]


