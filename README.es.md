# GameVault API

🇺🇸 [Read in English](README.md)

Una API RESTful para gestionar un catálogo de videojuegos — construida con **Java 21**, **Spring Boot 4** y **PostgreSQL**. Soporta operaciones CRUD completas, relaciones muchos-a-muchos con géneros, filtrado dinámico, paginación y manejo estructurado de errores.

> Proyecto de portafolio desarrollado para practicar backend profesional con Spring Boot, JPA/Hibernate y Docker.

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4 |
| ORM | Spring Data JPA / Hibernate |
| Base de datos | PostgreSQL (Docker) |
| Build | Maven |
| Documentación | Swagger / OpenAPI (springdoc) |
| Pruebas | Postman |

---

## Funcionalidades

- **CRUD completo** — crear, leer, actualizar y eliminar videojuegos
- **Relaciones Many-to-Many** — un juego puede tener múltiples géneros y viceversa, gestionado mediante una tabla de unión
- **Filtrado dinámico** — filtra juegos por título, género, precio o año de lanzamiento mediante query params
- **Paginación** — todas las respuestas de lista están paginadas
- **DTOs y Mappers** — separación limpia entre la capa de entidades y la capa de API
- **Validaciones** — validación del cuerpo de la petición con Jakarta Validation (`@NotBlank`, `@Min`, `@Max`, etc.)
- **Excepciones personalizadas** — excepciones específicas por caso de negocio como `GameNotFoundException` y `GameAlreadyExistsException`
- **Respuestas de error consistentes** — `ErrorResponse` estructurado con timestamp, status y path
- **Swagger UI** — documentación interactiva disponible en `/swagger-ui.html`

---

## Estructura del Proyecto

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

## Endpoints

Base URL: `/api/v1/games`

### Juegos

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/` | Obtener todos los juegos (paginado) |
| `GET` | `/?genre=RPG` | Filtrar por género |
| `GET` | `/?title=hollow` | Filtrar por título (coincidencia parcial) |
| `GET` | `/?price=19.99` | Filtrar por precio |
| `GET` | `/?year=2022` | Filtrar por año de lanzamiento |
| `GET` | `/?genre=RPG&price=19.99` | Filtrar por género y precio |
| `POST` | `/` | Crear un nuevo juego |
| `PUT` | `/{id}` | Actualizar un juego por ID |
| `DELETE` | `/{id}` | Eliminar un juego por ID |

Todos los `GET /` soportan los query params `page` y `size` (valores por defecto: `page=0`, `size=20`).

---

### Ejemplos de Request / Response

**POST /api/v1/games**

Cuerpo de la petición:
```json
{
  "title": "Hollow Knight",
  "price": 14.99,
  "releaseYear": 2017,
  "genreIds": [1, 3]
}
```

Respuesta `201 Created`:
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

**Ejemplo de error (404):**
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

## Cómo Ejecutarlo Localmente

### Requisitos previos

- Java 21
- Maven
- Docker

### 1. Clonar el repositorio

```bash
git clone https://github.com/MaxMini64/gamevault-api.git
cd gamevault-api
```

### 2. Levantar PostgreSQL con Docker

```bash
docker run --name gamevault-db \
  -e POSTGRES_DB=gamevault_db \
  -e POSTGRES_USER=tu_usuario \
  -e POSTGRES_PASSWORD=tu_contraseña \
  -p 5432:5432 \
  -d postgres
```

### 3. Configurar la aplicación

Crea el archivo `src/main/resources/application.properties` (está en el `.gitignore`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gamevault_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Decisiones de Diseño

**Estrategia de paginación en dos queries** — en lugar de usar una sola query paginada con `JOIN FETCH` (lo cual genera el warning `HHH90003004` de Hibernate y produce conteos incorrectos en relaciones Many-to-Many), el repositorio primero obtiene una página de IDs y luego carga las entidades completas con sus géneros usando `JOIN FETCH` y una cláusula `IN`. Esto evita el problema de N+1 queries sin sacrificar la precisión de la paginación.

**Excepciones por caso de negocio** — cada escenario de error tiene su propia clase de excepción (`GameNotFoundException`, `GameAlreadyExistsException`, `GenreNotFoundException`), lo que hace al `GlobalExceptionHandler` predecible y fácil de extender.

**Records para DTOs** — `GameRequest`, `GameResponse` y `GenreResponse` usan Java records, que son inmutables y concisos por defecto.

---

## Roadmap

- [ ] Endpoints CRUD para géneros
- [ ] Migraciones de base de datos con Flyway
- [ ] Tests unitarios e de integración (JUnit + Testcontainers)
- [ ] Docker Compose para levantar el proyecto completo
- [ ] Autenticación con Spring Security + JWT
- [ ] Módulo de IA: búsqueda en lenguaje natural usando tool calling

---

## Autor

**Máximo Flores García**  
Estudiante de Ingeniería en Tecnologías Computacionales — Tecnológico de Monterrey
