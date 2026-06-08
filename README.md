# GameVault API

🇲🇽 [Leer en español](README.es.md)

A RESTful API for managing a video game catalog — built with **Java 21**, **Spring Boot 4**, and **PostgreSQL**. Supports full CRUD operations, many-to-many genre relationships, dynamic filtering, pagination, and structured error handling.

> Built as a portfolio project to practice professional backend development with Spring Boot, JPA/Hibernate, and Docker.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL (Docker) |
| Build Tool | Maven |
| Documentation | Swagger / OpenAPI (springdoc) |
| Testing | Postman |

---

## Features

- **Full CRUD** — create, read, update, and delete video games
- **Many-to-Many relationships** — games can belong to multiple genres and vice versa, managed via a join table
- **Dynamic filtering** — filter games by title, genre, price, or release year via query params
- **Pagination** — all list responses are paginated
- **DTOs and Mappers** — clean separation between entity and API layers
- **Validation** — request body validation using Jakarta Validation (`@NotBlank`, `@Min`, `@Max`, etc.)
- **Custom exceptions** — domain-specific exceptions like `GameNotFoundException` and `GameAlreadyExistsException`
- **Consistent error responses** — structured `ErrorResponse` with timestamp, status, and path
- **Swagger UI** — interactive API documentation available at `/swagger-ui.html`

---

## Project Structure

```
src/main/java/com/gv/game_vault/
├── GameVaultApplication.java
├── config/
│   └── OpenAPIConfig.java
└── games/
    ├── controller/
    │   └── GameController.java
    ├── service/
    │   └── GameService.java
    ├── repository/
    │   ├── GameRepository.java
    │   └── GenreRepository.java
    ├── entity/
    │   ├── Game.java
    │   └── Genre.java
    ├── dto/
    │   ├── GameRequest.java
    │   ├── GameResponse.java
    │   └── GenreResponse.java
    ├── mapper/
    │   └── GameMapper.java
    └── exception/
        ├── GlobalExceptionHandler.java
        ├── GameNotFoundException.java
        ├── GameAlreadyExistsException.java
        ├── GenreNotFoundException.java
        └── ErrorResponse.java
```

---

## API Endpoints

Base URL: `/api/v1/games`

### Games

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/` | Get all games (paginated) |
| `GET` | `/?genre=RPG` | Filter by genre |
| `GET` | `/?title=hollow` | Filter by title (partial match) |
| `GET` | `/?price=19.99` | Filter by price |
| `GET` | `/?year=2022` | Filter by release year |
| `GET` | `/?genre=RPG&price=19.99` | Filter by genre and price |
| `POST` | `/` | Create a new game |
| `PUT` | `/{id}` | Update a game by ID |
| `DELETE` | `/{id}` | Delete a game by ID |

All `GET /` responses support `page` and `size` query params (defaults: `page=0`, `size=20`).

---

### Request / Response Examples

**POST /api/v1/games**

Request body:
```json
{
  "title": "Hollow Knight",
  "price": 14.99,
  "releaseYear": 2017,
  "genreIds": [1, 3]
}
```

Response `201 Created`:
```json
{
  "id": 5,
  "title": "Hollow Knight",
  "price": 14.99,
  "releaseYear": 2017,
  "genres": [
    { "id": 1, "name": "Action" },
    { "id": 3, "name": "Platformer" }
  ]
}
```

**Error response example (404):**
```json
{
  "timestamp": "2025-06-01T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Game with id 99 was not found",
  "path": "/api/v1/games/99"
}
```

---

## Running Locally

### Prerequisites

- Java 21
- Maven
- Docker

### 1. Clone the repository

```bash
git clone https://github.com/MaxMini64/gamevault-api.git
cd gamevault-api
```

### 2. Start PostgreSQL with Docker

```bash
docker run --name gamevault-db \
  -e POSTGRES_DB=gamevault_db \
  -e POSTGRES_USER=your_user \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 \
  -d postgres
```

### 3. Configure the application

Create `src/main/resources/application.properties` (this file is gitignored):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gamevault_db
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Design Decisions

**Two-query pagination strategy** — instead of using a single paginated query with `JOIN FETCH` (which causes Hibernate's `HHH90003004` warning and incorrect counts with Many-to-Many), the repository first fetches a page of IDs, then fetches the full entities with their genres using `JOIN FETCH` and an `IN` clause. This avoids N+1 queries while keeping pagination accurate.

**Custom exceptions per domain case** — each error scenario has its own exception class (`GameNotFoundException`, `GameAlreadyExistsException`, `GenreNotFoundException`), making the `GlobalExceptionHandler` predictable and easy to extend.

**Records for DTOs** — `GameRequest`, `GameResponse`, and `GenreResponse` use Java records, which are immutable and concise by default.

---

## Roadmap

- [ ] Genres CRUD endpoints
- [ ] Database migrations with Flyway
- [ ] Unit and integration tests (JUnit + Testcontainers)
- [ ] Docker Compose setup for the full stack
- [ ] Spring Security + JWT authentication
- [ ] AI module: natural language game search using tool calling

---

## Author

**Máximo Flores García**  
Computer Engineering Student — Tecnológico de Monterrey
