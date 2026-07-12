package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.CourseDropoutRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentResponse;
import com.example.coursemanagementsystem.dto.EnrolledStudentResponse;
import java.util.List;

public interface IStudentEnrollmentService {
    CourseEnrollmentResponse enrollStudent(Long courseId, CourseEnrollmentRequest request);

    void dropStudent(Long courseId, CourseDropoutRequest request);

    List<EnrolledStudentResponse> findCourseStudents(Long courseId, String search);
}
