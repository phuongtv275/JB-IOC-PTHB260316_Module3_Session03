package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.model.Enrollment;
import com.example.coursemanagementsystem.service.EnrollmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Enrollment>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok("Fetched enrollments successfully", enrollmentService.getAllEnrollments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Fetched enrollment successfully", enrollmentService.getEnrollmentById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Enrollment>> create(@RequestBody Enrollment enrollment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Enrollment created", enrollmentService.createEnrollment(enrollment)));
    }

    @PostMapping("/enroll-course")
    public ResponseEntity<ApiResponse<EnrollmentDetail>> enrollCourse(@RequestBody EnrollCourseRequest request) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Enrollment successful", enrollmentService.enrollCourse(request)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> update(@PathVariable Long id, @RequestBody Enrollment enrollment) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Enrollment updated", enrollmentService.updateEnrollment(id, enrollment)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Enrollment>> delete(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollmentById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
}
