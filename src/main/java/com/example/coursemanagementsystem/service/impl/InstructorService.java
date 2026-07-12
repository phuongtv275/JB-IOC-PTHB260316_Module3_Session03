package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.CourseInstructorResponse;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.dto.InstructorCreateRequest;
import com.example.coursemanagementsystem.dto.InstructorResponse;
import com.example.coursemanagementsystem.dto.InstructorUpdateRequest;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.repository.StudentEnrollmentRepository;
import com.example.coursemanagementsystem.service.IInstructorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorService implements IInstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final StudentEnrollmentRepository enrollmentRepository;

    @Override
    public List<InstructorResponse> getAllInstructors() {
        return instructorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public InstructorResponse getInstructorById(Long id) {
        return toResponse(findInstructor(id));
    }

    @Override
    public InstructorResponse createInstructor(InstructorCreateRequest request) {
        Instructor instructor = new Instructor();
        instructor.setName(request.name());
        instructor.setEmail(request.email());
        return toResponse(instructorRepository.save(instructor));
    }

    @Override
    public InstructorResponse updateInstructor(Long id, InstructorUpdateRequest request) {
        Instructor instructor = findInstructor(id);
        instructor.setName(request.name());
        instructor.setEmail(request.email());
        return toResponse(instructorRepository.save(instructor));
    }

    @Override
    public void deleteInstructorById(Long id) {
        instructorRepository.delete(findInstructor(id));
    }

    @Override
    public List<InstructorDetail> getInstructorDetails() {
        return instructorRepository.findAll().stream()
                .map(instructor -> new InstructorDetail(toResponse(instructor), activeEnrolledCourses(instructor.getId())))
                .toList();
    }

    private List<CourseResponse> activeEnrolledCourses(Long instructorId) {
        return courseRepository.findAll().stream()
                .filter(course -> instructorId.equals(course.getInstructor().getId()))
                .filter(course -> CourseStatus.ACTIVE == course.getStatus())
                .filter(course -> enrollmentRepository.existsByCourseId(course.getId()))
                .map(this::toCourseResponse)
                .toList();
    }

    private Instructor findInstructor(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    }

    private InstructorResponse toResponse(Instructor instructor) {
        return new InstructorResponse(instructor.getId(), instructor.getName(), instructor.getEmail());
    }

    private CourseResponse toCourseResponse(Course course) {
        Instructor instructor = course.getInstructor();
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                new CourseInstructorResponse(instructor.getId(), instructor.getName())
        );
    }
}
