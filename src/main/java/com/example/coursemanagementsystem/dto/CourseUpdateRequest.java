package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.CourseStatus;

public record CourseUpdateRequest(String title, CourseStatus status, Long instructorId) {
}
