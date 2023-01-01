# RSO: Searcher microservice

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar searcher-api-1.0.0-SNAPSHOT.jar
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

Health: localhost:8081/health/live

Metrics:localhost:8081/health/metrics

GraphQL: localhost:8081/graphiql

OpenAPI: localhost:8081/api-specs/ui/
