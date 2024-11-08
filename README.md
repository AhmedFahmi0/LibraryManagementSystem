## Technologies
- Spring boot
- postgres

## Features
- Task Scheduling: Automates tasks at specified intervals.
- Aspects (AOP): Implements cross-cutting concerns such as logging.
- Caching: Enhances performance by storing frequently accessed data.
- Logging: Provides detailed logs for debugging and monitoring.
- Authentication: Secures endpoints using JWT (JSON Web Token).
- Auditing: Tracks and records changes to data for accountability and transparency.

## Running the Application
To run the application locally, follow these steps:
### Prerequisites:
- Make sure you have Java 11 or higher installed on your machine.
### Clone this repository to your local machine.
```
git clone https://github.com/AhmedFahmi0/LibraryManagementSystem.git
```
### Navigate to the project directory in your terminal.
```
cd libraryManagementSystem
```
### Run the following command to start the application:
```
./mvnw spring-boot:run
```
### If using PostgreSQL container: 
- Run the following command to start the PostgreSQL container
```
docker-compose up
```
- Once the application has started, you can access it at `http://localhost:8080`.

## Documentation
`http://localhost:8080/swagger-ui/index.html`
