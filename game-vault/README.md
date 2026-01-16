# 🎮 GameVault API

GameVault es una API RESTful diseñada para gestionar una biblioteca de videojuegos. Permite administrar juegos, precios, años de lanzamiento y géneros, implementando relaciones complejas y filtros de búsqueda avanzados.

Este proyecto fue desarrollado utilizando las mejores prácticas de **Spring Boot** y **JPA** para crear un backend robusto y escalable.

## 🚀 Tecnologías Utilizadas

* **Lenguaje:** Java 25 (OpenJDK)
* **Framework:** Spring Boot 4
* **Base de Datos:** PostgreSQL
* **ORM:** Hibernate / Spring Data JPA
* **Herramientas:** Maven, Git, Postman
* **IDE:** IntelliJ IDEA

## ⚙️ Funcionalidades Principales

* **CRUD Completo:** Creación, lectura, actualización y eliminación de videojuegos.
* **Relaciones Many-to-Many:** Gestión eficiente de géneros (un juego puede tener múltiples géneros y viceversa) sin redundancia de datos.
* **Filtrado Inteligente:** Endpoints dinámicos para buscar juegos por:
    * Título
    * Año de lanzamiento
    * Precio
    * Género específico
* **Manejo de Errores:** Respuestas HTTP personalizadas (409 Conflict para duplicados, 404 Not Found, etc.) para una mejor experiencia de cliente.

## 🔧 Configuración e Instalación

Como este proyecto protege las credenciales de la base de datos, sigue estos pasos para ejecutarlo localmente:

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/TU_USUARIO/GameVault-api.git](https://github.com/TU_USUARIO/GameVault-api.git)
    ```

2.  **Base de Datos:**
    Asegúrate de tener PostgreSQL instalado y crea una base de datos llamada: `gamevault_db`.

3.  **Configuración de Secretos:**
    Crea el archivo `src/main/resources/application.properties` y agrega tus credenciales locales:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/gamevault_db
    spring.datasource.username=TU_USUARIO
    spring.datasource.password=TU_CONTRASEÑA
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

4.  **Ejecutar:**
    Corre el proyecto desde tu IDE o usando Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📡 Ejemplos de Endpoints

| Método | URL | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/v1/game` | Obtener todos los juegos |
| `GET` | `/api/v1/game?genre=Action` | Buscar juegos por género |
| `POST` | `/api/v1/game` | Registrar un nuevo juego |
| `DELETE`| `/api/v1/game/{id}` | Eliminar un juego por ID |

---
**Desarrollado por Máximo Flores García**
*Estudiante de Ingeniería en Tecnologías Computacionales - Tec de Monterrey*