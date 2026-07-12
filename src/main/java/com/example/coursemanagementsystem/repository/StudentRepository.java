package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            select new com.example.coursemanagementsystem.dto.StudentResponse(student.id, student.name, student.email)
            from Student student
            where lower(student.name) like lower(concat('%', :keyword, '%'))
            """)
    Page<StudentResponse> findStudentSummaries(@Param("keyword") String keyword, Pageable pageable);
}
