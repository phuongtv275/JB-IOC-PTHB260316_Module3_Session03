package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.model.Course;
import java.util.List;

public interface ICourseService {
    List<Course> getAllCourses();

    Course getCourseById(Long id);

    Course createCourse(Course course);

    Course updateCourse(Long id, Course course);

    Course deleteCourseById(Long id);
}
