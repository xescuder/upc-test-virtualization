## TDD with Spring Boot, Mockito and JUnit 5


```shell
mvn clean install -DskipTests
docker build .
docker-compose down -v
docker-compose up -d
```


Open Postman and create a new request: `localhost:8090/api/users`

You should get:

```json
[
    {
        "id": 1,
        "firstName": "Xavier",
        "lastName": "Escudero Sabadell",
        "email": "xescuder@gmail.com",
        "password": null,
        "registrationDate": null
    }
]
```



## Test Environments and Service Virtualization

### Test Environments

- **Local**: Run the application locally and use an in-memory database.
- **Docker**: Run the application in a Docker container and use an in-memory database.
- 
