package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.StudentCreateRequest;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.dto.PageResponse;
import org.springframework.data.domain.Sort;

public interface IStudentService {
    PageResponse<StudentResponse> getAllStudents(int page, int size, String sortBy, Sort.Direction direction, String keyword);

    StudentResponse getStudentById(Long id);

    StudentResponse createStudent(StudentCreateRequest request);
}
