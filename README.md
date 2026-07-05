# Course Management System

Spring Boot REST API for Session03 exercises. The app manages instructors, courses, and enrollments with a simple layered architecture:

- `model`: data models
- `repository`: in-memory seed data and CRUD
- `service`: business logic
- `controller`: REST endpoints and `ApiResponse` wrappers
- `dto`: request/response DTOs

## Requirements

- Java 17+
- Maven Wrapper included: `./mvnw`

On this machine, use the bundled JDK path:

```bash
export JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11
```

## Run

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw spring-boot:run
```

Base URL:

```text
http://localhost:8080
```

## Test

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw test
```

## API Endpoints

### Instructors

- `GET /instructors`
- `GET /instructors/{id}`
- `POST /instructors`
- `PUT /instructors/{id}`
- `DELETE /instructors/{id}`
- `GET /instructors/details`

### Courses

- `GET /courses`
- `GET /courses/{id}`
- `POST /courses`
- `PUT /courses/{id}`
- `DELETE /courses/{id}`

### Enrollments

- `GET /enrollments`
- `GET /enrollments/{id}`
- `POST /enrollments`
- `PUT /enrollments/{id}`
- `DELETE /enrollments/{id}`
- `POST /enrollments/enroll-course`

## Example Request

```bash
curl -X POST http://localhost:8080/enrollments/enroll-course \
  -H 'Content-Type: application/json' \
  -d '{"id":100,"studentName":"Student test","courseId":1}'
```

Expected response:

```json
{
  "success": true,
  "message": "Enrollment successful",
  "data": {
    "id": 100,
    "studentName": "Student test",
    "course": {
      "id": 1,
      "title": "Intro Java",
      "status": "ACTIVE",
      "instructorId": 1
    }
  }
}
```

## Postman

Import this collection into Postman:

```text
postman/CourseManagementSystem.postman_collection.json
```

Manual testing steps are documented in:

```text
docs/testing-scenario.md
```
