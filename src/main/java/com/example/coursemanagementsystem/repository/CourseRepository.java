package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    List<Course> findAll();

    Optional<Course> findById(Long id);

    Course create(Course course);

    Course update(Long id, Course course);

    Course deleteById(Long id);
}
