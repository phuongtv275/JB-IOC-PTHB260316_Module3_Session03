package com.example.coursemanagementsystem.service.impl;

import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.exception.ResourceNotFoundException;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.EnrollmentRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.service.IInstructorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorService implements IInstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public InstructorService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    }

    @Override
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.create(instructor);
    }

    @Override
    public Instructor updateInstructor(Long id, Instructor instructor) {
        return instructorRepository.update(id, instructor);
    }

    @Override
    public Instructor deleteInstructorById(Long id) {
        return instructorRepository.deleteById(id);
    }

    @Override
    public List<InstructorDetail> getInstructorDetails() {
        return instructorRepository.findAll().stream()
                .map(instructor -> new InstructorDetail(instructor, activeEnrolledCourses(instructor.getId())))
                .toList();
    }

    private List<Course> activeEnrolledCourses(Long instructorId) {
        return courseRepository.findAll().stream()
                .filter(course -> instructorId.equals(course.getInstructorId()))
                .filter(course -> CourseStatus.ACTIVE == course.getStatus())
                .filter(course -> enrollmentRepository.existsByCourseId(course.getId()))
                .toList();
    }
}
