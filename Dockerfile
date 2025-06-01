FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

EXPOSE 8080

CMD ["java", "-jar", "build/libs/test-0.0.1-SNAPSHOT.jar"]
