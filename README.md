# Auth Service

Microservicio de autenticacion y usuarios para **Explore Costa Rica Tours**.

Implementa registro, login, sesiones por token UUID almacenadas en base de datos, endpoint `/auth/me`, logout y administracion de usuarios protegida por rol `ADMIN`.

## Stack

| Componente | Tecnologia |
| --- | --- |
| Runtime | Java 21 |
| Framework | Spring Boot 3.3 |
| Seguridad | Spring Security 6 |
| Persistencia | Spring Data JPA |
| Base local | H2 |
| Base produccion | PostgreSQL |
| Migraciones | Flyway |
| Documentacion | SpringDoc OpenAPI |
| Build | Maven |

## Ejecutar Local

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Servicio:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

## Variables

Crear variables locales con base en `.env.example`. Para desarrollo, el perfil `local` usa H2 en memoria y no necesita PostgreSQL.

Para crear el primer admin automaticamente, configurar:

```env
ADMIN_EMAIL=admin@explorecr.com
ADMIN_PASSWORD=Admin1234!
```

Si `ADMIN_PASSWORD` esta vacio, no se crea ningun admin inicial.

## Endpoints

```http
POST /auth/register
POST /auth/login
GET /auth/me
POST /auth/logout
GET /users
GET /users/{id}
PUT /users/{id}
DELETE /users/{id}
GET /health
```

Los endpoints protegidos usan:

```http
Authorization: Bearer <session-uuid>
```

## Respuestas

Auth retorna:

```json
{
  "data": {
    "token": "uuid",
    "user": {
      "id": "uuid",
      "name": "Juan Costa",
      "email": "juan@explorecr.com",
      "role": "user",
      "avatar": null,
      "createdAt": "2026-05-01T10:00:00"
    }
  }
}
```

Errores retornan:

```json
{
  "error": "Invalid email or password",
  "status": 401
}
```

## Produccion

Perfil Render PostgreSQL:

```env
SPRING_PROFILE=render
RENDER_POSTGRES_URL=jdbc:postgresql://host:5432/database
RENDER_POSTGRES_USER=user
RENDER_POSTGRES_PASSWORD=password
```

El Dockerfile genera el JAR con Maven y corre la aplicacion en Java 21.
