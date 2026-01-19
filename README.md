# PersonalMovieCatalog

Aplicacion de seguimiento de peliculas construida con Spring Boot. Busca peliculas, guardalas en tu watchlist, calificalas y escribe reseñas.

## Stack Tecnologico

- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- H2 Database (archivo local)
- Thymeleaf + Bootstrap 5
- TMDB API

## Configuracion

### Requisitos

- Java 17+
- Maven 3.8+
- TMDB API Key (obtener en https://www.themoviedb.org/settings/api)

### Configurar API Key

Editar `src/main/resources/application.properties`:

```properties
tmdb.api.key=TU_BEARER_TOKEN_AQUI
```

### Ejecutar

```bash
mvn spring-boot:run
```

Acceder: http://localhost:8080

### Consola H2

URL: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:file:./data/personalmoviecatalog`  
Usuario: `sa`  
Password: (vacio)

## Funcionalidades

- Registro e inicio de sesion
- Busqueda de peliculas via TMDB
- Mostrar peliculas populares
- Guardar peliculas como "Pendiente" o "Vista"
- Calificar peliculas (1-5 estrellas)
- Escribir reseñas personales
- Filtrar por estado

## Estructura del Proyecto

```
src/main/java/com/personalmoviecatalog/
├── config/         # Configuracion de seguridad
├── controller/     # Controladores MVC
├── dto/            # Objetos de transferencia
├── model/          # Entidades JPA
├── repository/     # Capa de acceso a datos
└── service/        # Logica de negocio
```

## Licencia

MIT
