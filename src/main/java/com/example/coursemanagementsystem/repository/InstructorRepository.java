package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Instructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InstructorRepository {
    private final List<Instructor> instructors = new ArrayList<>(List.of(
            new Instructor(1L, "Nguyen Van A", "a@example.com"),
            new Instructor(2L, "Tran Thi B", "b@example.com")
    ));

    public List<Instructor> findAll() {
        return instructors;
    }

    public Optional<Instructor> findById(Long id) {
        return instructors.stream().filter(instructor -> instructor.getId().equals(id)).findFirst();
    }

    public Instructor create(Instructor instructor) {
        instructors.add(instructor);
        return instructor;
    }

    public Instructor update(Long id, Instructor instructor) {
        Instructor existing = findById(id).orElseThrow(() -> new RuntimeException("Instructor not found"));
        existing.setName(instructor.getName());
        existing.setEmail(instructor.getEmail());
        return existing;
    }

    public Instructor deleteById(Long id) {
        Instructor existing = findById(id).orElseThrow(() -> new RuntimeException("Instructor not found"));
        instructors.remove(existing);
        return existing;
    }
}
