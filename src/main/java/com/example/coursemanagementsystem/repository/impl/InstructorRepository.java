package com.example.coursemanagementsystem.repository.impl;

import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Instructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InstructorRepository implements com.example.coursemanagementsystem.repository.InstructorRepository {
    private final List<Instructor> instructors = new ArrayList<>(List.of(
            new Instructor(1L, "Nguyen Van A", "a@example.com"),
            new Instructor(2L, "Tran Thi B", "b@example.com")
    ));

    @Override
    public List<Instructor> findAll() {
        return instructors;
    }

    @Override
    public Optional<Instructor> findById(Long id) {
        return instructors.stream().filter(instructor -> instructor.getId().equals(id)).findFirst();
    }

    @Override
    public Instructor create(Instructor instructor) {
        instructors.add(instructor);
        return instructor;
    }

    @Override
    public Instructor update(Long id, Instructor instructor) {
        Instructor existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        existing.setName(instructor.getName());
        existing.setEmail(instructor.getEmail());
        return existing;
    }

    @Override
    public Instructor deleteById(Long id) {
        Instructor existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        instructors.remove(existing);
        return existing;
    }
}
