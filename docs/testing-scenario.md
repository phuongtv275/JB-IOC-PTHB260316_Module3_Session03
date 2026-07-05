# Course Management System Testing Scenario

Base URL: `http://localhost:8080`

Start the app before running:

```bash
JAVA_HOME=/home/trgphun/.jdks/ms-21.0.11 ./mvnw spring-boot:run
```

## Scenario 1: Seed Data And ApiResponse

1. `GET /courses`
   - Expected status: `200`
   - Expected body: `success = true`, message is `Fetched courses successfully`, data contains `Intro Java`

2. `GET /instructors`
   - Expected status: `200`
   - Expected body: at least two instructors

3. `GET /enrollments`
   - Expected status: `200`
   - Expected body: at least two enrollments

## Scenario 2: Basic Course CRUD

1. `POST /courses`
   - Body: `{"id":10,"title":"REST API","status":"ACTIVE","instructorId":1}`
   - Expected status: `201`
   - Expected body: message is `Course created`

2. `GET /courses/10`
   - Expected status: `200`
   - Expected body: title is `REST API`

3. `PUT /courses/10`
   - Body: `{"title":"REST API Updated","status":"INACTIVE","instructorId":1}`
   - Expected status: `200`
   - Expected body: title is `REST API Updated`

4. `DELETE /courses/10`
   - Expected status: `204`

5. `GET /courses/10`
   - Expected status: `404`
   - Expected body: `success = false`, message is `Course not found`

## Scenario 3: Enrollment Business Rules

1. `POST /enrollments/enroll-course`
   - Body: `{"id":100,"studentName":"Student test","courseId":1}`
   - Expected status: `200`
   - Expected body: message is `Enrollment successful`, returned course status is `ACTIVE`

2. `POST /enrollments/enroll-course`
   - Body: `{"id":101,"studentName":"Student test","courseId":2}`
   - Expected status: `400`
   - Expected body: message is `Cannot enroll in an inactive course`

3. `POST /enrollments/enroll-course`
   - Body: `{"id":102,"studentName":"Student test","courseId":999}`
   - Expected status: `400`
   - Expected body: message is `Course not found`

## Scenario 4: Instructor Detail Business Rule

1. `GET /instructors/details`
   - Expected status: `200`
   - Expected body: each course in each instructor detail is `ACTIVE`
   - Expected body: seeded instructor 1 includes course `Intro Java`

