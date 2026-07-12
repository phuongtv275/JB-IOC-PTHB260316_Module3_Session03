package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.StudentCreateRequest;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.repository.StudentRepository;
import com.example.coursemanagementsystem.service.IStudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;

    @Override
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        return toResponse(studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found")));
    }

    @Override
    public StudentResponse createStudent(StudentCreateRequest request) {
        Student student = new Student();
        student.setName(request.name());
        student.setEmail(request.email());
        return toResponse(studentRepository.save(student));
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(student.getId(), student.getName(), student.getEmail());
    }
}
