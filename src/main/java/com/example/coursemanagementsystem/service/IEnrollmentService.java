package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.model.Enrollment;
import java.util.List;

public interface IEnrollmentService {
    List<Enrollment> getAllEnrollments();

    Enrollment getEnrollmentById(Long id);

    Enrollment createEnrollment(Enrollment enrollment);

    Enrollment updateEnrollment(Long id, Enrollment enrollment);

    Enrollment deleteEnrollmentById(Long id);

    EnrollmentDetail enrollCourse(EnrollCourseRequest request);
}
