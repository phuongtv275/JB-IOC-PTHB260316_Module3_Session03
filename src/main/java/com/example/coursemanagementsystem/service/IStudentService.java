package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.StudentCreateRequest;
import com.example.coursemanagementsystem.dto.StudentResponse;
import java.util.List;

public interface IStudentService {
    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Long id);

    StudentResponse createStudent(StudentCreateRequest request);
}
