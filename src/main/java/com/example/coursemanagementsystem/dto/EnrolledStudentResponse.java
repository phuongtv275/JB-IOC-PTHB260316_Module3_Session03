package com.example.coursemanagementsystem.dto;

import java.time.LocalDateTime;

public record EnrolledStudentResponse(Long id, String name, String email, LocalDateTime enrolledAt) {
}
