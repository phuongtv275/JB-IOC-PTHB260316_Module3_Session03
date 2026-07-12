package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseResponseV2;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.model.CourseStatus;
import org.springframework.data.domain.Sort;

public interface ICourseService {
    PageResponse<CourseResponseV2> getAllCourses(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction,
            CourseStatus status,
            String keyword
    );

    CourseResponse getCourseById(Long id);

    CourseResponse createCourse(CourseCreateRequest request);

    CourseResponse updateCourse(Long id, CourseUpdateRequest request);

    void deleteCourseById(Long id);
}
