package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.model.Instructor;
import java.util.List;

public interface IInstructorService {
    List<Instructor> getAllInstructors();

    Instructor getInstructorById(Long id);

    Instructor createInstructor(Instructor instructor);

    Instructor updateInstructor(Long id, Instructor instructor);

    Instructor deleteInstructorById(Long id);

    List<InstructorDetail> getInstructorDetails();
}
