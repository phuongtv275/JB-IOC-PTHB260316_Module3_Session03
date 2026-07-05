package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.exception.BusinessException;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Enrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.EnrollmentRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.service.IEnrollmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService implements IEnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            CourseRepository courseRepository,
            InstructorRepository instructorRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
    }

    @Override
    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentRepository.create(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        return enrollmentRepository.update(id, enrollment);
    }

    @Override
    public Enrollment deleteEnrollmentById(Long id) {
        return enrollmentRepository.deleteById(id);
    }

    @Override
    public EnrollmentDetail enrollCourse(EnrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new BusinessException("Course not found"));

        if (CourseStatus.ACTIVE != course.getStatus()) {
            throw new BusinessException("Cannot enroll in an inactive course");
        }

        instructorRepository.findById(course.getInstructorId())
                .orElseThrow(() -> new BusinessException("Instructor not found"));

        if (enrollmentRepository.existsByStudentNameAndCourseId(request.getStudentName(), request.getCourseId())) {
            throw new BusinessException("Student already enrolled in this course");
        }

        Enrollment enrollment = enrollmentRepository.create(
                new Enrollment(request.getId(), request.getStudentName(), request.getCourseId())
        );
        return new EnrollmentDetail(enrollment.getId(), enrollment.getStudentName(), course);
    }
}
