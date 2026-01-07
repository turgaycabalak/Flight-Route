# Flight Route Service

This project is a RESTful API. It calculates all valid travel routes between two locations by combining different transportation methods (Flight, Bus, Uber, Subway).

The system implements a dynamic routing algorithm that adheres to specific transfer rules and filters results based on **Operating Days**.

## 📋 Project Features

- [x] **Spring Boot REST API:** Developed using Java 21 and Spring Boot 4.x.
- [x] **Database Integration:** PostgreSQL used with Hibernate (ORM) for data persistence.
- [x] **CRUD Operations:** API endpoints for managing `Locations` and `Transportations`.
- [x] **Routing Algorithm:** Calculates routes based on the logic: *Transfer (Optional) -> Flight (Mandatory) -> Transfer (Optional)*.
- [x] **API Documentation:** Integrated Swagger UI (SpringDoc OpenAPI).
- [x] **Operating Days:** Implemented logic to filter transportations based on specific days of the week (e.g., a bus that only runs on Mondays and Wednesdays).
- [x] **Caching:** Caffeine Cache implementation for improved performance on route queries.

## 🛠 Tech Stack

* **Java 21**
* **Spring Boot 4.0.1**
* **PostgreSQL**
* **Spring Data JPA / Hibernate**
* **MapStruct** (For clear Entity-DTO mapping)
* **Caffeine Cache**
* **SpringDoc OpenAPI** (Swagger)

## 🚀 Setup & Running

1.  Clone the repository.
2.  Update the database configuration in `src/main/resources/application.yml` to match your local PostgreSQL setup:
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/postgres
        username: your_username
        password: your_password
    ```
3.  Build and run the project using Maven:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
4.  The application will start on port **9096**.

## 📖 API Documentation

Once the application is running, you can access the full API documentation and test endpoints via Swagger UI:

👉 **Swagger UI:** [http://localhost:9096/swagger-ui.html](http://localhost:9096/swagger-ui.html)

### Main Usage: Searching for Routes

To search for valid routes including the **date validation** (Operating Days check), use the following endpoint:

**Endpoint:** `GET /api/routes/v3`

**Parameters:**
* `originId`: ID of the starting location.
* `destinationId`: ID of the destination.
* `date`: Date of travel (Format: YYYY-MM-DD).

**Example Request:**
```http
GET /api/routes/v3?originId=1&destinationId=5&date=2025-03-12
```

## 📮 Postman Collection

For easy testing, a Postman collection containing sample requests is included in the project.

1.  Locate the file `Flight_Route_Service.postman_collection.json` in the project root directory.
2.  Import the file into Postman.
3.  The collection includes pre-configured requests for creating locations, transportations, and searching for routes.