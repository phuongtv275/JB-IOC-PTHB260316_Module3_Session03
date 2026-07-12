# Course Management System

Spring Boot REST API for the Rikkei Module 3 course-management exercises.

The app manages instructors, courses, students, and course enrollments with a layered structure:

- `model`: JPA entities and relationships
- `repository`: Spring Data JPA repositories
- `service`: business logic and DTO mapping
- `controller`: REST endpoints with `ApiResponse`
- `dto`: request/response DTOs
- `exception`: API error handling

## Tech Stack

- Java 17+
- Spring Boot Web MVC
- Spring Data JPA
- PostgreSQL
- Lombok
- H2 for tests
- Maven Wrapper: `./mvnw`

## Database Config

Default local config in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rikkei-postgres
spring.datasource.username=rikkei
spring.datasource.password=rikkei
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

For Docker app containers, override the datasource with environment variables:

```bash
docker run --name rikkei-app \
  --network rikkei-network \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://rikkei-postgres:5432/rikkei-postgres \
  -e SPRING_DATASOURCE_USERNAME=rikkei \
  -e SPRING_DATASOURCE_PASSWORD=rikkei \
  -d your-spring-image
```

## Run Locally

PostgreSQL must be running and reachable on `localhost:5432`.

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw spring-boot:run
```

Base URL:

```text
http://localhost:8080
```

## Test

Tests use H2, so PostgreSQL is not required for this command:

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw test
```

## API Endpoints

### Instructors

- `GET /instructors`
- `GET /instructors/{id}`
- `GET /instructors/details`
- `POST /instructors`
- `PUT /instructors/{id}`
- `DELETE /instructors/{id}`

### Courses

- `GET /courses`
- `GET /courses/{id}`
- `POST /courses`
- `PUT /courses/{id}`
- `DELETE /courses/{id}`
- `POST /courses/{courseId}/enrollments`
- `GET /courses/{courseId}/enrollments/students?search={keyword}`
- `DELETE /courses/{courseId}/enrollments/students/{studentId}`
- `DELETE /courses/{courseId}/students/{studentId}`

### Students

- `GET /students`
- `GET /students/{id}`
- `POST /students`

### Student Enrollments

- `POST /students-enrollments`

## Example Flow

Create instructor:

```bash
curl -X POST http://localhost:8080/instructors \
  -H 'Content-Type: application/json' \
  -d '{"name":"Nguyen Van A","email":"a@example.com"}'
```

Create course:

```bash
curl -X POST http://localhost:8080/courses \
  -H 'Content-Type: application/json' \
  -d '{"title":"Spring Data JPA","status":"ACTIVE","instructorId":1}'
```

Create student:

```bash
curl -X POST http://localhost:8080/students \
  -H 'Content-Type: application/json' \
  -d '{"name":"Tran Thi B","email":"b@example.com"}'
```

Enroll student:

```bash
curl -X POST http://localhost:8080/courses/1/enrollments \
  -H 'Content-Type: application/json' \
  -d '{"studentId":1}'
```

Successful enrollment response:

```json
{
  "success": true,
  "message": "Enrollment created",
  "data": {
    "studentId": 1,
    "courseId": 1,
    "enrolledAt": "2026-07-13T10:00:00"
  }
}
```

## Postman

Import:

```text
postman/CourseManagementSystem.postman_collection.json
```

Manual scenarios:

```text
docs/testing-scenario.md
```
