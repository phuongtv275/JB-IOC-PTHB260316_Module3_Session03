package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import java.util.List;

public interface ICourseService {
    List<CourseResponse> getAllCourses();

    CourseResponse getCourseById(Long id);

    CourseResponse createCourse(CourseCreateRequest request);

    CourseResponse updateCourse(Long id, CourseUpdateRequest request);

    void deleteCourseById(Long id);
}
