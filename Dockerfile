FROM amazoncorretto:18
RUN mkdir /app

WORKDIR /app

ADD ./api/target/searcher-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "searcher-api-1.0.0-SNAPSHOT.jar"]
