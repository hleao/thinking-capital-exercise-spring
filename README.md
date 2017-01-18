# thinking-capital-exercise

# Coding exercise for thinking capital

This project aims to build a Java REST service that will integrate with a JMS system.

The Java REST service uses Spring Boot for it's REST API implementation. For JMS system, this project is using Spring JMS template and an embedded ActiveMQ broker.


####Running this project
```
mvn spring-boot:run
```
Server running at http://localhost:8080

####Running the tests
```
mvn test  - Unit tests (Will test message filtering)
mvn verify - "Integration Test" (Will test the complete flow from receiving the messages pack on the REST endpoint to receiving the expected JMS messages on a mocked JMS receiver)
```
