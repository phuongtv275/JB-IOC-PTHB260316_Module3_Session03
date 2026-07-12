package com.example.coursemanagementsystem.dto;

import java.time.LocalDateTime;

public record CourseEnrollmentResponse(Long studentId, Long courseId, LocalDateTime enrolledAt) {
}
