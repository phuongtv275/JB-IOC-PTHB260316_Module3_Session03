package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseDropoutRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentResponse;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseResponseV2;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import com.example.coursemanagementsystem.dto.EnrolledStudentResponse;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.service.ICourseService;
import com.example.coursemanagementsystem.service.IStudentEnrollmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final ICourseService courseService;
    private final IStudentEnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponseV2>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Sort.Direction direction,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Fetched courses successfully",
                courseService.getAllCourses(page, size, sortBy, direction, status, keyword)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Fetched course successfully", courseService.getCourseById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> create(@RequestBody CourseCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Course created", courseService.createCourse(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> update(@PathVariable Long id, @RequestBody CourseUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Course updated", courseService.updateCourse(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/enrollments")
    public ResponseEntity<ApiResponse<CourseEnrollmentResponse>> enroll(
            @PathVariable Long courseId,
            @RequestBody CourseEnrollmentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Enrollment created", enrollmentService.enrollStudent(courseId, request)));
    }

    @DeleteMapping({"/{courseId}/students/{studentId}", "/{courseId}/enrollments/students/{studentId}"})
    public ResponseEntity<ApiResponse<Void>> drop(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ) {
        enrollmentService.dropStudent(courseId, new CourseDropoutRequest(studentId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/enrollments/students")
    public ResponseEntity<ApiResponse<List<EnrolledStudentResponse>>> findCourseStudents(
            @PathVariable Long courseId,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Fetched enrolled students successfully",
                enrollmentService.findCourseStudents(courseId, search)
        ));
    }
}
