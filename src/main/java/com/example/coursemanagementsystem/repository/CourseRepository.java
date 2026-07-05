package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Course;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository {
    private final List<Course> courses = new ArrayList<>(List.of(
            new Course(1L, "Intro Java", "ACTIVE", 1L),
            new Course(2L, "Spring MVC", "INACTIVE", 2L)
    ));

    public List<Course> findAll() {
        return courses;
    }

    public Optional<Course> findById(Long id) {
        return courses.stream().filter(course -> course.getId().equals(id)).findFirst();
    }

    public Course create(Course course) {
        courses.add(course);
        return course;
    }

    public Course update(Long id, Course course) {
        Course existing = findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        existing.setTitle(course.getTitle());
        existing.setStatus(course.getStatus());
        existing.setInstructorId(course.getInstructorId());
        return existing;
    }

    public Course deleteById(Long id) {
        Course existing = findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        courses.remove(existing);
        return existing;
    }
}
