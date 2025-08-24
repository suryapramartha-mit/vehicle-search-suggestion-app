# Vehicle Search & Suggestion App
A Spring Boot application for Vehicle Service Search and Operation Suggestion.

### Assumption
- Database stores all distances in kilometers
- Test data is provided in kilometers
- Support distance conversion unit (1 mile = 1.60934 km)

### Installation & Running Locally
Prerequisites
- Java 17+
- Spring Boot (latest stable version)
- Maven
- H2 Database (in-memory for testing)
- Git

Installation 
- Clone the repository 
- Build the app `mvn clean install`
- Run application `mvn spring-boot:run`

The application will start on the default port `8080`.

- API Documentation on `http://localhost:8080/swagger-ui.html`
- Testing Database on `http://localhost:8080/h2-console`

