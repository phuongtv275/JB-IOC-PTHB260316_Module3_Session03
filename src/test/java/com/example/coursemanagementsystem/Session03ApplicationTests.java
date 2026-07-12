package com.example.coursemanagementsystem;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.repository.StudentEnrollmentRepository;
import com.example.coursemanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Session03ApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentEnrollmentRepository enrollmentRepository;

    @BeforeEach
    void cleanDatabase() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        instructorRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void coursesAreReturnedWithApiResponse() throws Exception {
        Course course = saveCourse("Intro Java", CourseStatus.ACTIVE);

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Fetched courses successfully"))
                .andExpect(jsonPath("$.data[0].id").value(course.getId()))
                .andExpect(jsonPath("$.data[0].title").value("Intro Java"))
                .andExpect(jsonPath("$.data[0].instructor.name").value(course.getInstructor().getName()));
    }

    @Test
    void missingCourseUsesApiResponseError() throws Exception {
        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void courseCrudUsesInstructorIdRequestAndDtoResponse() throws Exception {
        Instructor instructor = saveInstructor("Teacher One", "teacher1@example.com");

        String created = mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"REST API","status":"ACTIVE","instructorId":%d}
                                """.formatted(instructor.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Course created"))
                .andExpect(jsonPath("$.data.title").value("REST API"))
                .andReturn().getResponse().getContentAsString();

        Long courseId = Long.valueOf(created.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(put("/courses/" + courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"REST API Updated","status":"INACTIVE","instructorId":%d}
                                """.formatted(instructor.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("REST API Updated"))
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));

        mockMvc.perform(delete("/courses/" + courseId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/courses/" + courseId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudentReturnsDto() throws Exception {
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Student One","email":"student1@example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Student created"))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.name").value("Student One"));
    }

    @Test
    void enrollCourseReturnsDetailForActiveCourse() throws Exception {
        Course course = saveCourse("Intro Java", CourseStatus.ACTIVE);
        Student student = saveStudent("Student test", "student@example.com");

        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"studentId":%d}
                                """.formatted(student.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Enrollment created"))
                .andExpect(jsonPath("$.data.studentId").value(student.getId()))
                .andExpect(jsonPath("$.data.courseId").value(course.getId()))
                .andExpect(jsonPath("$.data.enrolledAt", notNullValue()));
    }

    @Test
    void enrollCourseRejectsDuplicateStudentForCourse() throws Exception {
        Course course = saveCourse("Intro Java", CourseStatus.ACTIVE);
        Student student = saveStudent("Duplicate Student", "duplicate@example.com");
        String requestBody = """
                {"studentId":%d}
                """.formatted(student.getId());

        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Student already enrolled in this course"));
    }

    @Test
    void enrollCourseRejectsInactiveCourse() throws Exception {
        Course course = saveCourse("Inactive Java", CourseStatus.INACTIVE);
        Student student = saveStudent("Student test", "student@example.com");

        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"studentId":%d}
                                """.formatted(student.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot enroll in an inactive course"));
    }

    @Test
    void searchAndDropCourseStudents() throws Exception {
        Course course = saveCourse("Intro Java", CourseStatus.ACTIVE);
        Student alice = saveStudent("Alice Nguyen", "alice@example.com");
        Student bob = saveStudent("Bob Tran", "bob@example.com");

        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":%d}".formatted(alice.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":%d}".formatted(bob.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/courses/" + course.getId() + "/enrollments/students?search=alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(alice.getId()));

        mockMvc.perform(delete("/courses/" + course.getId() + "/enrollments/students/" + alice.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/courses/" + course.getId() + "/enrollments/students?search=alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void instructorDetailsOnlyIncludeActiveCoursesWithEnrollments() throws Exception {
        Course course = saveCourse("Intro Java", CourseStatus.ACTIVE);
        Student student = saveStudent("Student test", "student@example.com");
        mockMvc.perform(post("/courses/" + course.getId() + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":%d}".formatted(student.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/instructors/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].courses[0].status").value("ACTIVE"));
    }

    private Course saveCourse(String title, CourseStatus status) {
        Instructor instructor = saveInstructor("Nguyen Van A", "a@example.com");
        Course course = new Course();
        course.setTitle(title);
        course.setStatus(status);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    private Instructor saveInstructor(String name, String email) {
        Instructor instructor = new Instructor();
        instructor.setName(name);
        instructor.setEmail(email);
        return instructorRepository.save(instructor);
    }

    private Student saveStudent(String name, String email) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        return studentRepository.save(student);
    }
}
