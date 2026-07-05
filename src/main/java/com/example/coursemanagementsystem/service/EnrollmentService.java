package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.Enrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.EnrollmentRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
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

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        return enrollmentRepository.create(enrollment);
    }

    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        return enrollmentRepository.update(id, enrollment);
    }

    public Enrollment deleteEnrollmentById(Long id) {
        return enrollmentRepository.deleteById(id);
    }

    public EnrollmentDetail enrollCourse(EnrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!"ACTIVE".equals(course.getStatus())) {
            throw new RuntimeException("Cannot enroll in an inactive course");
        }

        instructorRepository.findById(course.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Enrollment enrollment = enrollmentRepository.create(
                new Enrollment(request.getId(), request.getStudentName(), request.getCourseId())
        );
        return new EnrollmentDetail(enrollment.getId(), enrollment.getStudentName(), course);
    }
}
