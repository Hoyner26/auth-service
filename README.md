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
http://localhost:8080/swagger-ui/index.html
```

Health check:

```text
http://localhost:8080/health
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

El microservicio esta desplegado en Azure App Service:

```text
https://explorecr-auth-service-d3gzhsdjazexh9er.eastus2-01.azurewebsites.net
```

Swagger en produccion:

```text
https://explorecr-auth-service-d3gzhsdjazexh9er.eastus2-01.azurewebsites.net/swagger-ui/index.html
```

Health check en produccion:

```text
https://explorecr-auth-service-d3gzhsdjazexh9er.eastus2-01.azurewebsites.net/health
```

Variables de entorno usadas en Azure:

```env
SPRING_PROFILE=render
RENDER_POSTGRES_URL=jdbc:postgresql://dpg-d8fp6i28qa3s73ah67bg-a.oregon-postgres.render.com:5432/explorecr_users
RENDER_POSTGRES_USER=explorecr_admin
RENDER_POSTGRES_PASSWORD=<password-de-render>
ADMIN_EMAIL=admin@explorecr.com
ADMIN_PASSWORD=<password-admin>
SESSION_EXPIRATION_HOURS=24
WEBSITES_PORT=8080
WEBSITES_CONTAINER_START_TIME_LIMIT=1800
```

La base de datos de produccion esta en Render PostgreSQL. La aplicacion corre en Azure App Service usando una imagen Docker publicada en Azure Container Registry mediante GitHub Actions.

## Despliegue

El workflow de GitHub Actions se ejecuta al hacer push a `main`.

Flujo:

```text
Maven build -> Docker build -> Push a ACR -> Deploy a Azure App Service
```

El Dockerfile genera el JAR con Maven y corre la aplicacion en Java 21.

## Integracion con otros microservicios

Los otros microservicios pueden validar una sesion llamando:

```http
GET /auth/me
Authorization: Bearer <session-uuid>
```

Si la sesion es valida, el servicio retorna los datos del usuario autenticado. Si no es valida, retorna `401 Unauthorized`.
