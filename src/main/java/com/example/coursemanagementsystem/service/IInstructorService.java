package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.dto.InstructorCreateRequest;
import com.example.coursemanagementsystem.dto.InstructorResponse;
import com.example.coursemanagementsystem.dto.InstructorUpdateRequest;
import java.util.List;

public interface IInstructorService {
    List<InstructorResponse> getAllInstructors();

    InstructorResponse getInstructorById(Long id);

    InstructorResponse createInstructor(InstructorCreateRequest request);

    InstructorResponse updateInstructor(Long id, InstructorUpdateRequest request);

    void deleteInstructorById(Long id);

    List<InstructorDetail> getInstructorDetails();
}
