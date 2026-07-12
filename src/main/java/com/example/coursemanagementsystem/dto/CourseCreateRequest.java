package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.CourseStatus;

public record CourseCreateRequest(String title, CourseStatus status, Long instructorId) {
}
