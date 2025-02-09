FROM openjdk:17

EXPOSE 8080

WORKDIR /app

COPY target/learningCenter.jar /app/learningCenter.jar

ENTRYPOINT ["java", "-jar", "learningCenter.jar"]
