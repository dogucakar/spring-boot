FROM amazoncorretto:21

ENV APP_HOME=/app

WORKDIR $APP_HOME

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]