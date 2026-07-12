package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseInstructorResponse;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.service.ICourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        return toResponse(findCourse(id));
    }

    @Override
    public CourseResponse createCourse(CourseCreateRequest request) {
        Course course = new Course();
        apply(course, request.title(), request.status(), request.instructorId());
        return toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseUpdateRequest request) {
        Course course = findCourse(id);
        apply(course, request.title(), request.status(), request.instructorId());
        return toResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.delete(findCourse(id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private void apply(Course course, String title, com.example.coursemanagementsystem.model.CourseStatus status, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        course.setTitle(title);
        course.setStatus(status);
        course.setInstructor(instructor);
    }

    private CourseResponse toResponse(Course course) {
        Instructor instructor = course.getInstructor();
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                new CourseInstructorResponse(instructor.getId(), instructor.getName())
        );
    }
}
