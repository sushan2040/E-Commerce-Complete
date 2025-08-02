FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ecommerce-0.0.1-SNAPSHOT.jar app.jar
ENV DB_URL=${DB_URL} DB_USERNAME=${DB_USERNAME} DB_PASSWORD=${DB_PASSWORD}
ENTRYPOINT ["java", "-jar", "app.jar"]