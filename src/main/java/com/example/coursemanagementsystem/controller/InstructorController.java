package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.dto.InstructorCreateRequest;
import com.example.coursemanagementsystem.dto.InstructorResponse;
import com.example.coursemanagementsystem.dto.InstructorUpdateRequest;
import com.example.coursemanagementsystem.service.IInstructorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/instructors")
public class InstructorController {
    private final IInstructorService instructorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstructorResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok("Fetched instructors successfully", instructorService.getAllInstructors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Fetched instructor successfully", instructorService.getInstructorById(id)));
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<List<InstructorDetail>>> details() {
        return ResponseEntity.ok(ApiResponse.ok("Fetched instructor details successfully", instructorService.getInstructorDetails()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstructorResponse>> create(@RequestBody InstructorCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Instructor created", instructorService.createInstructor(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponse>> update(
            @PathVariable Long id,
            @RequestBody InstructorUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok("Instructor updated", instructorService.updateInstructor(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        instructorService.deleteInstructorById(id);
        return ResponseEntity.noContent().build();
    }
}
