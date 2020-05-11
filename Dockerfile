FROM maven:3-openjdk-11 as builder

COPY . /app

WORKDIR /app
RUN mvn -B clean install


FROM openjdk:11 as runner

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
