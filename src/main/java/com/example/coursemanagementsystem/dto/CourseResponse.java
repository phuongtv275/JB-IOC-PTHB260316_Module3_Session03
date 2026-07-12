package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.CourseStatus;

public record CourseResponse(Long id, String title, CourseStatus status, CourseInstructorResponse instructor) {
}
