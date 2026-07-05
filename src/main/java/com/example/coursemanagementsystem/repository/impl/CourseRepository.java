package com.example.coursemanagementsystem.repository.impl;

import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.exception.BusinessException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository implements com.example.coursemanagementsystem.repository.CourseRepository {
    private final List<Course> courses = new ArrayList<>(List.of(
            new Course(1L, "Intro Java", CourseStatus.ACTIVE, 1L),
            new Course(2L, "Spring MVC", CourseStatus.INACTIVE, 2L)
    ));

    @Override
    public List<Course> findAll() {
        return courses;
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courses.stream().filter(course -> course.getId().equals(id)).findFirst();
    }

    @Override
    public Course create(Course course) {
        if (findById(course.getId()).isPresent()) {
            throw new BusinessException("Course id already exists");
        }
        courses.add(course);
        return course;
    }

    @Override
    public Course update(Long id, Course course) {
        Course existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        existing.setTitle(course.getTitle());
        existing.setStatus(course.getStatus());
        existing.setInstructorId(course.getInstructorId());
        return existing;
    }

    @Override
    public Course deleteById(Long id) {
        Course existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        courses.remove(existing);
        return existing;
    }
}
