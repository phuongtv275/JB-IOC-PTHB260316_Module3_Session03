package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Enrollment;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    List<Enrollment> findAll();

    Optional<Enrollment> findById(Long id);

    boolean existsByCourseId(Long courseId);

    boolean existsByStudentNameAndCourseId(String studentName, Long courseId);

    Enrollment create(Enrollment enrollment);

    Enrollment update(Long id, Enrollment enrollment);

    Enrollment deleteById(Long id);
}
