# Library Management System

## Technologies
- Spring boot
- postgres

## Running the Application
To run the application locally, follow these steps:

- Make sure you have Java 11 or higher installed on your machine.
- Clone this repository to your local machine. `git clone https://github.com/AhmedFahmi0/LibraryManagementSystem.git`
- Navigate to the project directory in your terminal.
- Run the following command to start the application:
```
./mvnw spring-boot:run
```
- if using PostgreSQL container 
- Run the following command to start the PostgreSQL container `docker-compose up`.
- Once the application has started, you can access it at `http://localhost:8080`.

## Authentication
To authenticate, include the access token in the Authorization header of your requests.
Example:
Authorization: Bearer <access-token>

## Documentation
`http://localhost:8080/swagger-ui/index.html`
