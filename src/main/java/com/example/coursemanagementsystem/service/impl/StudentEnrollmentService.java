package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.CourseDropoutRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentResponse;
import com.example.coursemanagementsystem.dto.EnrolledStudentResponse;
import com.example.coursemanagementsystem.exception.BusinessException;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.StudentEnrollmentRepository;
import com.example.coursemanagementsystem.repository.StudentRepository;
import com.example.coursemanagementsystem.service.IStudentEnrollmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentEnrollmentService implements IStudentEnrollmentService {
    private final StudentEnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public CourseEnrollmentResponse enrollStudent(Long courseId, CourseEnrollmentRequest request) {
        Course course = findCourse(courseId);
        if (CourseStatus.ACTIVE != course.getStatus()) {
            throw new BusinessException("Cannot enroll in an inactive course");
        }

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, request.studentId())) {
            throw new BusinessException("Student already enrolled in this course");
        }

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        return toEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional
    public void dropStudent(Long courseId, CourseDropoutRequest request) {
        findCourse(courseId);
        StudentEnrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public List<EnrolledStudentResponse> findCourseStudents(Long courseId, String search) {
        findCourse(courseId);
        String keyword = search == null || search.isBlank() ? null : search.trim();
        return enrollmentRepository.findCourseStudents(courseId, keyword).stream()
                .map(enrollment -> new EnrolledStudentResponse(
                        enrollment.getStudent().getId(),
                        enrollment.getStudent().getName(),
                        enrollment.getStudent().getEmail(),
                        enrollment.getEnrolledAt()
                ))
                .toList();
    }

    private Course findCourse(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private CourseEnrollmentResponse toEnrollmentResponse(StudentEnrollment enrollment) {
        return new CourseEnrollmentResponse(
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getEnrolledAt()
        );
    }
}
