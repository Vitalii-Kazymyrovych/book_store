# Builder stage
FROM openjdk:17-jdk-slim as builder
WORKDIR bookstore
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bookstore.jar
RUN java -Djarmode=layertools -jar bookstore.jar extract

# Final stage
FROM openjdk:17-jdk-slim
WORKDIR bookstore
COPY --from=builder bookstore/dependencies/ ./
COPY --from=builder bookstore/spring-boot-loader/ ./
COPY --from=builder bookstore/snapshot-dependencies/ ./
COPY --from=builder bookstore/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
