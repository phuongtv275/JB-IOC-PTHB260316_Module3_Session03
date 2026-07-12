package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.CourseStatus;

public record CourseResponseV2(Long id, String title, CourseStatus status) {
}
