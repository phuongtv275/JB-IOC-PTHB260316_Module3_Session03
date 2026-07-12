package com.example.coursemanagementsystem.dto;

import java.util.List;

public record InstructorDetail(InstructorResponse instructor, List<CourseResponse> courses) {
}
