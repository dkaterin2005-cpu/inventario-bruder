FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src
# Construimos el JAR
RUN ./mvnw clean package -DskipTests
FROM eclipse-temurin:17-jre
WORKDIR /app
# El JAR final
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]


