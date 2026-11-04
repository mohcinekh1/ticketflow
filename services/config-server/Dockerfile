FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -B -ntp -pl services/config-server -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/config-server/target/*.jar app.jar
COPY config-repo /config-repo
ENV CONFIG_REPO_LOCATION=file:/config-repo
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
