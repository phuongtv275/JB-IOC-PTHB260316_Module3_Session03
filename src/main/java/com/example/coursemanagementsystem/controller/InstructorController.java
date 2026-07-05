package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.service.InstructorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/instructors")
public class InstructorController {
    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Instructor>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok("Fetched instructors successfully", instructorService.getAllInstructors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Fetched instructor successfully", instructorService.getInstructorById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<List<InstructorDetail>>> details() {
        return ResponseEntity.ok(ApiResponse.ok("Fetched instructor details successfully", instructorService.getInstructorDetails()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Instructor>> create(@RequestBody Instructor instructor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Instructor created", instructorService.createInstructor(instructor)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> update(@PathVariable Long id, @RequestBody Instructor instructor) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Instructor updated", instructorService.updateInstructor(id, instructor)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> delete(@PathVariable Long id) {
        try {
            instructorService.deleteInstructorById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }
}
