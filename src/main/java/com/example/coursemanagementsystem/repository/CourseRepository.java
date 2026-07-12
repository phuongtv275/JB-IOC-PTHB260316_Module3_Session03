package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.dto.CourseResponseV2;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndStatus(Long id, CourseStatus status);

    @Query("""
            select new com.example.coursemanagementsystem.dto.CourseResponseV2(course.id, course.title, course.status)
            from Course course
            where (:status is null or course.status = :status)
              and lower(course.title) like lower(concat('%', :keyword, '%'))
            """)
    Page<CourseResponseV2> findCourseSummaries(
            @Param("status") CourseStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
