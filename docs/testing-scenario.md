# Course Management System Testing Scenario

Base URL: `http://localhost:8080`

Start PostgreSQL container with database `rikkei-postgres`, user `rikkei`, password `rikkei`, then run locally:

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw spring-boot:run
```

Run app container on the same Docker network:

```bash
docker run --name rikkei-app \
  --network rikkei-network \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://rikkei-postgres:5432/rikkei-postgres \
  -e SPRING_DATASOURCE_USERNAME=rikkei \
  -e SPRING_DATASOURCE_PASSWORD=rikkei \
  -d your-spring-image
```

## Scenario 1: Instructor And Course Flow

1. `POST /instructors`
   - Body: `{"name":"Nguyen Van A","email":"a@example.com"}`
   - Expected status: `201`
   - Expected body: `success = true`, message is `Instructor created`, data contains generated `id`

2. `POST /courses`
   - Body: `{"title":"Spring Data JPA","status":"INACTIVE","instructorId":<instructorId>}`
   - Expected status: `201`
   - Expected body: `data.instructor.id = <instructorId>`

3. `PUT /courses/<courseId>`
   - Body: `{"title":"Spring Data JPA","status":"ACTIVE","instructorId":<instructorId>}`
   - Expected status: `200`
   - Expected body: `data.status = ACTIVE`

4. `GET /courses`
   - Expected status: `200`
   - Expected body: courses are DTOs with `id`, `title`, `status`, `instructor`; no circular entity graph

## Scenario 2: Student Enrollment Flow

1. `POST /students`
   - Body: `{"name":"Tran Thi B","email":"b@example.com"}`
   - Expected status: `201`
   - Expected body: generated student `id`

2. `POST /courses/<courseId>/enrollments`
   - Body: `{"studentId":<studentId>}`
   - Expected status: `201`
   - Expected body: `data.studentId`, `data.courseId`, `data.enrolledAt`

3. `POST /courses/<courseId>/enrollments` again with the same body
   - Expected status: `400`
   - Expected body: message is `Student already enrolled in this course`

4. `GET /courses/<courseId>/enrollments/students?search=Tran`
   - Expected status: `200`
   - Expected body: matching student appears in `data`

5. `DELETE /courses/<courseId>/enrollments/students/<studentId>`
   - Expected status: `204`

## Scenario 3: Compatibility Endpoint

1. `POST /students-enrollments`
   - Body: `{"studentId":<studentId>,"courseId":<courseId>}`
   - Expected status: `201`
   - Expected body shape matches `POST /courses/<courseId>/enrollments`

## Scenario 4: Error Cases

1. `POST /courses`
   - Body: `{"title":"Invalid Course","status":"ACTIVE","instructorId":999999}`
   - Expected status: `404`
   - Expected body: message is `Instructor not found`

2. `POST /courses/<inactiveCourseId>/enrollments`
   - Body: `{"studentId":<studentId>}`
   - Expected status: `400`
   - Expected body: message is `Cannot enroll in an inactive course`

3. `GET /courses/999999`
   - Expected status: `404`
   - Expected body: message is `Course not found`

## Automated Check

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw test
```
