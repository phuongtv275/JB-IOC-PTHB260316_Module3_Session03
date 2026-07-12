package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.CourseEnrollmentRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentResponse;
import com.example.coursemanagementsystem.dto.StudentEnrollmentCreateRequest;
import com.example.coursemanagementsystem.service.IStudentEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students-enrollments")
public class StudentEnrollmentController {
    private final IStudentEnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseEnrollmentResponse>> create(
            @RequestBody StudentEnrollmentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(
                        "Enrollment created",
                        enrollmentService.enrollStudent(request.courseId(), new CourseEnrollmentRequest(request.studentId()))
                ));
    }
}
