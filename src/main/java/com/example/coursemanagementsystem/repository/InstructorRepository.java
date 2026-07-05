package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Instructor;
import java.util.List;
import java.util.Optional;

public interface InstructorRepository {
    List<Instructor> findAll();

    Optional<Instructor> findById(Long id);

    Instructor create(Instructor instructor);

    Instructor update(Long id, Instructor instructor);

    Instructor deleteById(Long id);
}
