# RSO: Searcher microservice

## Prerequisites

```bash
docker run -d --name pg-image-metadata -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=image-metadata -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar image-catalog-api-1.0.0-SNAPSHOT.jar
```

## Run in IntelliJ IDEA
Add new Run configuration and select the Application type. In the next step, select the module api and for the main class com.kumuluz.ee.EeApplication.

## Docker commands
```bash
docker build -t searcher .   
docker images
docker run searcher    
docker tag searcher kkklemennn/searcher   
docker push kkklemennn/searcher
docker ps
```

## URLs

Health: localhost:8080/health/live

Metrics:localhost:8080/health/metrics

GraphQL: localhost:8080/graphiql

OpenAPI: localhost:8080/api-specs/ui/
