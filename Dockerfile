FROM openjdk:17
COPY target/bookstore.jar bookstore.jar
ENTRYPOINT ["java", "-jar", "/bookstore.jar"]
EXPOSE 8080