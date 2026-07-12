package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseInstructorResponse;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseResponseV2;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public PageResponse<CourseResponseV2> getAllCourses(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction,
            CourseStatus status,
            String keyword
    ) {
        return PageResponse.from(courseRepository.findCourseSummaries(
                status,
                normalize(keyword),
                pageable(page, size, sortBy, direction)
        ));
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

    private void apply(Course course, String title, CourseStatus status, Long instructorId) {
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

    private Pageable pageable(int page, int size, String sortBy, Sort.Direction direction) {
        int safePage = Math.max(page, 0);
        int safeSize = size > 0 ? size : 10;
        if (direction == null) {
            return PageRequest.of(safePage, safeSize);
        }
        String field = courseSortField(sortBy);
        return PageRequest.of(safePage, safeSize, Sort.by(direction, field));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }

    private String courseSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "id";
        }
        return switch (sortBy.trim()) {
            case "id", "title", "status" -> sortBy.trim();
            default -> "id";
        };
    }
}
