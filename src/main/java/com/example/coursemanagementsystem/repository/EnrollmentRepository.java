package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Enrollment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentRepository {
    private final List<Enrollment> enrollments = new ArrayList<>(List.of(
            new Enrollment(1L, "Student One", 1L),
            new Enrollment(2L, "Student Two", 1L)
    ));

    public List<Enrollment> findAll() {
        return enrollments;
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream().filter(enrollment -> enrollment.getId().equals(id)).findFirst();
    }

    public boolean existsByCourseId(Long courseId) {
        return enrollments.stream().anyMatch(enrollment -> enrollment.getCourseId().equals(courseId));
    }

    public Enrollment create(Enrollment enrollment) {
        enrollments.add(enrollment);
        return enrollment;
    }

    public Enrollment update(Long id, Enrollment enrollment) {
        Enrollment existing = findById(id).orElseThrow(() -> new RuntimeException("Enrollment not found"));
        existing.setStudentName(enrollment.getStudentName());
        existing.setCourseId(enrollment.getCourseId());
        return existing;
    }

    public Enrollment deleteById(Long id) {
        Enrollment existing = findById(id).orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollments.remove(existing);
        return existing;
    }
}
