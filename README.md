# thinking-capital-exercise

# Coding exercise for thinking capital

This project aims to build a Java REST service that will integrate with a JMS system.

The Java REST service uses Spring Boot for it's REST API implementation. For JMS system, this project is using Spring JMS template and an embedded ActiveMQ broker.


####Runnin this project
```
mvn spring-boot:run
```
Server running at http://localhost:8080

####Runnin the tests
```
mvn test  - Unit tests (Will test message filtering)
mvn verify - "Integration Test" (Will test the complete flow of receiving the message on the REST endpoint, sending and receiving the JMS message)
```
