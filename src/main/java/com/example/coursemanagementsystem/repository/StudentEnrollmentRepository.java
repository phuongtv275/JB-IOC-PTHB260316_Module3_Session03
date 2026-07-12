package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.StudentEnrollment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {
    boolean existsByCourseId(Long courseId);

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    Optional<StudentEnrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    @Query("""
            select enrollment
            from StudentEnrollment enrollment
            where enrollment.course.id = :courseId
              and (:search is null
                   or lower(enrollment.student.name) like lower(concat('%', :search, '%')))
            """)
    List<StudentEnrollment> findCourseStudents(
            @Param("courseId") Long courseId,
            @Param("search") String search
    );
}
