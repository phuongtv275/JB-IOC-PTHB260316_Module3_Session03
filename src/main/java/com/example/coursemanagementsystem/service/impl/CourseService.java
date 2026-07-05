package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.service.ICourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService implements ICourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.create(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        return courseRepository.update(id, course);
    }

    @Override
    public Course deleteCourseById(Long id) {
        return courseRepository.deleteById(id);
    }
}
