# TechManager API

A RESTful API for user management, built with **Spring Boot 3**, **Java 21**, **Spring Data JPA**, **Flyway**, and **Actuator**.  
The database is **PostgreSQL**, running in containers (Docker Compose for development, Kubernetes for production).  
Tests are implemented with **JUnit 5**, **Mockito**, and **Testcontainers**.

---

## Features

- CRUD for `User` entity
- Data validation with Bean Validation (email, phone, birth date)
- Database migrations with Flyway (drift prevention)
- Health endpoints (`/actuator/health`, readiness and liveness probes)
- Unit tests (service layer) + integration tests (MockMvc + Testcontainers)
- Profiles: `dev` (Docker Compose) and `prod` (Kubernetes)

---

## Main Dependencies

- Spring Web  
- Spring Data JPA  
- Validation  
- Lombok  
- Flyway  
- Actuator  
- PostgreSQL Driver  
- Testcontainers (Postgres + JUnit 5)  

---

## Running the Application

### 1. Local (without Docker)
```bash
# Run directly with Maven
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Or build and run the JAR
./mvnw -q -DskipTests package
java -jar target/*SNAPSHOT.jar --spring.profiles.active=dev
```

Requires a local PostgreSQL running on port 5432 (or adjust `application-dev.properties`).

---

### 2. With Docker Compose (recommended)

Create a `.env` file in the project root (do not commit this file):

```env
POSTGRES_USER=tech
POSTGRES_PASSWORD=techpass
POSTGRES_DB=techmanager
SPRING_PROFILES_ACTIVE=dev
```

Build and start containers:

```bash
docker compose down -v
./mvnw -q -DskipTests package
docker compose --env-file .env up --build
```

Access:

* API: [http://localhost:8080/api/users](http://localhost:8080/api/users)
* Health: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

---

## Running Tests

Run all tests:

```bash
./mvnw clean test
```

Run only unit tests:

```bash
./mvnw -Dtest=*ServiceTest test
```

Run only integration tests:

```bash
./mvnw -Dtest=*IntegrationTest test
```

Integration tests use **Testcontainers** (real PostgreSQL in Docker).

---

## Build & Push Docker Image (Production)

```bash
./mvnw -q -DskipTests package
docker build -t ghcr.io/<your-user>/techmanager:prod .
docker push ghcr.io/<your-user>/techmanager:prod
```

---

## Deploy to Kubernetes

### 1. Create ConfigMap from `.env`

```bash
kubectl create configmap techmanager-env --from-env-file=.env --dry-run=client -o yaml | kubectl apply -f -
```

### 2. Apply manifests

```bash
kubectl apply -f k8s/postgres-statefulset.yaml
kubectl apply -f k8s/app-deployment.yaml
```

### 3. Test

```bash
kubectl port-forward svc/techmanager 8080:80
curl -s http://localhost:8080/actuator/health
```

## Health Probes (K8s)

* Readiness: `/actuator/health/readiness`
* Liveness: `/actuator/health/liveness`

---

## API Endpoints

All endpoints are available under the `/api/users` path.

### Create User

Creates a new user. The request body must contain the user's data. On success, it returns `201 Created` with the newly created user object, including its generated `id`.

* **Endpoint:** `POST /api/users`
* **Headers:** `Content-Type: application/json`
* **Body:**

```json
{
  "fullName": "Ana Silva",
  "email": "ana@example.com",
  "phone": "+55 11 99999-9999",
  "birthDate": "1990-01-01",
  "userType": "ADMIN"
}
```

**Example:**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "fullName":"Ana Silva",
    "email":"ana@example.com",
    "phone":"+55 11 99999-9999",
    "birthDate":"1990-01-01",
    "userType":"ADMIN"
  }'
```

### List Users

Retrieves a list of all registered users.

  * **Endpoint:** `GET /api/users`
  * **Example:**

```bash
curl http://localhost:8080/api/users
```

### Get by ID

Retrieves a single user by their unique ID. If the ID is not found, it returns a `404 Not Found` error.

  * **Endpoint:** `GET /api/users/{id}`
  * **Example:**

```bash
curl http://localhost:8080/api/users/1
```

### Update User

Updates the data of an existing user identified by their ID. The request body must contain the new data for the user.

  * **Endpoint:** `PUT /api/users/{id}`
  * **Headers:** `Content-Type: application/json`
  * **Body:**

```json
{
  "fullName": "Ana Maria Silva",
  "email": "ana.maria@example.com",
  "phone": "+55 11 98888-8888",
  "birthDate": "1990-01-01",
  "userType": "EDITOR"
}
```

**Example:**

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullName":"Ana Maria Silva",
    "email":"ana.maria@example.com",
    "phone":"+55 11 98888-8888",
    "birthDate":"1990-01-01",
    "userType":"EDITOR"
  }'
```

### Delete User

Deletes a user by their unique ID. On success, it returns `204 No Content`. If the ID is not found, it returns a `404 Not Found` error.

  * **Endpoint:** `DELETE /api/users/{id}`
  * **Example:**

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

---

## Package Structure

```
src/main/java/com/techmanager/techmanager/
 ├── TechmanagerApplication.java
 ├── common/
 │    ├── ApiExceptionHandler.java
 │    └── ErrorResponse.java
 └── user/
      ├── User.java
      ├── UserType.java
      ├── UserRepository.java
      ├── UserService.java
      ├── UserController.java
      ├── UserMapper.java
      ├── NotFoundException.java
      └── dto/
           ├── UserRequest.java
           └── UserResponse.java
```