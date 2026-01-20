# --- 1. Etapa de Build ---
# Usa uma imagem completa do JDK para compilar o projeto com Maven
FROM amazoncorretto:21.0.3-alpine3.19 as build

ARG POSTGRES_PASSWORD_ARG
ARG DB_HOST_ARG
ARG DB_PORT_ARG

ENV POSTGRES_PASSWORD=$POSTGRES_PASSWORD_ARG
ENV DB_HOST=$DB_HOST_ARG
ENV DB_PORT=$DB_PORT_ARG

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

#Executa uma substituição de quebra de linhas caso o sistema operacional seja windows
RUN sed -i 's/\r$//' mvnw

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline


# Compila o projeto e cria o JAR. Os testes são pulados, pois não temos um banco de dados aqui.
RUN ./mvnw package -DskipTests

# --- 2. Etapa Final ---
# Usa uma imagem JRE (Java Runtime Environment) muito menor, apenas para executar a aplicação
# Usando a mesma versão do Java da etapa de build para garantir compatibilidade
FROM amazoncorretto:21.0.3-alpine3.19

# Cria um grupo e um usuário de sistema dedicados para rodar a aplicação
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Define o usuário que executará os próximos comandos e a aplicação
USER appuser


# Define o diretório de trabalho dentro do container
WORKDIR /app


# Copia o JAR da etapa de build para a imagem final, já com o usuário e grupo corretos
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar

# Comando para executar a aplicação quando o container iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]
