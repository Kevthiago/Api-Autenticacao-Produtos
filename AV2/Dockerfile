# Etapa 1: Build do projeto usando Maven com Java 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia os arquivos do projeto para dentro da imagem
COPY . .

# Executa o build do projeto (gera o .jar)
RUN mvn clean package -DskipTests

# Etapa 2: Cria a imagem final apenas com o JAR
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia o .jar da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
