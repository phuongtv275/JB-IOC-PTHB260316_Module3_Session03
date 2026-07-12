package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndStatus(Long id, CourseStatus status);
}
