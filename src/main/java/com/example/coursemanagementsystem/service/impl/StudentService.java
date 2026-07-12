package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.StudentCreateRequest;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.repository.StudentRepository;
import com.example.coursemanagementsystem.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository studentRepository;

    @Override
    public PageResponse<StudentResponse> getAllStudents(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction,
            String keyword
    ) {
        return PageResponse.from(studentRepository.findStudentSummaries(normalize(keyword), pageable(page, size, sortBy, direction)));
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

    private Pageable pageable(int page, int size, String sortBy, Sort.Direction direction) {
        int safePage = Math.max(page, 0);
        int safeSize = size > 0 ? size : 10;
        if (direction == null) {
            return PageRequest.of(safePage, safeSize);
        }
        String field = studentSortField(sortBy);
        return PageRequest.of(safePage, safeSize, Sort.by(direction, field));
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }

    private String studentSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "id";
        }
        return switch (sortBy.trim()) {
            case "id", "name", "email" -> sortBy.trim();
            default -> "id";
        };
    }
}
