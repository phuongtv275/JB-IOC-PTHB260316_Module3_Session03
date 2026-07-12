package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.dto.StudentCreateRequest;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final IStudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Sort.Direction direction,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Fetched students successfully",
                studentService.getAllStudents(page, size, sortBy, direction, keyword)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Fetched student successfully", studentService.getStudentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@RequestBody StudentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Student created", studentService.createStudent(request)));
    }
}
