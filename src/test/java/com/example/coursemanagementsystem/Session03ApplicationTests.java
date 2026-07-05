package com.example.coursemanagementsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Session03ApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void coursesAreReturnedWithApiResponse() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Fetched courses successfully"))
                .andExpect(jsonPath("$.data[0].title").value("Intro Java"));
    }

    @Test
    void missingCourseUsesApiResponseError() throws Exception {
        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void enrollCourseReturnsDetailForActiveCourse() throws Exception {
        mockMvc.perform(post("/enrollments/enroll-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":100,"studentName":"Student test","courseId":1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Enrollment successful"))
                .andExpect(jsonPath("$.data.id").value(100))
                .andExpect(jsonPath("$.data.course.status").value("ACTIVE"));
    }

    @Test
    void enrollCourseRejectsInactiveCourse() throws Exception {
        mockMvc.perform(post("/enrollments/enroll-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":101,"studentName":"Student test","courseId":2}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot enroll in an inactive course"));
    }

    @Test
    void instructorDetailsOnlyIncludeActiveCoursesWithEnrollments() throws Exception {
        mockMvc.perform(get("/instructors/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].courses[0].status").value("ACTIVE"));
    }
}
