package com.example.coursemanagementsystem.repository.impl;

import com.example.coursemanagementsystem.exception.BusinessException;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Enrollment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentRepository implements com.example.coursemanagementsystem.repository.EnrollmentRepository {
    private final List<Enrollment> enrollments = new ArrayList<>(List.of(
            new Enrollment(1L, "Student One", 1L),
            new Enrollment(2L, "Student Two", 1L)
    ));

    @Override
    public List<Enrollment> findAll() {
        return enrollments;
    }

    @Override
    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream().filter(enrollment -> enrollment.getId().equals(id)).findFirst();
    }

    @Override
    public boolean existsByCourseId(Long courseId) {
        return enrollments.stream().anyMatch(enrollment -> enrollment.getCourseId().equals(courseId));
    }

    @Override
    public boolean existsByStudentNameAndCourseId(String studentName, Long courseId) {
        return enrollments.stream()
                .anyMatch(enrollment -> enrollment.getStudentName().equals(studentName)
                        && enrollment.getCourseId().equals(courseId));
    }

    @Override
    public Enrollment create(Enrollment enrollment) {
        if (findById(enrollment.getId()).isPresent()) {
            throw new BusinessException("Enrollment id already exists");
        }
        if (existsByStudentNameAndCourseId(enrollment.getStudentName(), enrollment.getCourseId())) {
            throw new BusinessException("Student already enrolled in this course");
        }
        enrollments.add(enrollment);
        return enrollment;
    }

    @Override
    public Enrollment update(Long id, Enrollment enrollment) {
        Enrollment existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        existing.setStudentName(enrollment.getStudentName());
        existing.setCourseId(enrollment.getCourseId());
        return existing;
    }

    @Override
    public Enrollment deleteById(Long id) {
        Enrollment existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollments.remove(existing);
        return existing;
    }
}
