package com.example.coursemanagementsystem.config;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.repository.StudentEnrollmentRepository;
import com.example.coursemanagementsystem.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {
    private static final int TARGET_RECORDS = 20;

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final StudentEnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedInstructors();
        seedStudents();
        seedCourses();
        seedEnrollments();
    }

    private void seedInstructors() {
        long count = instructorRepository.count();
        if (count >= TARGET_RECORDS) {
            return;
        }

        List<Instructor> instructors = new ArrayList<>();
        for (long index = count + 1; index <= TARGET_RECORDS; index++) {
            Instructor instructor = new Instructor();
            instructor.setName("Demo Instructor %02d".formatted(index));
            instructor.setEmail("instructor%02d@example.com".formatted(index));
            instructors.add(instructor);
        }
        instructorRepository.saveAll(instructors);
    }

    private void seedStudents() {
        long count = studentRepository.count();
        if (count >= TARGET_RECORDS) {
            return;
        }

        List<Student> students = new ArrayList<>();
        for (long index = count + 1; index <= TARGET_RECORDS; index++) {
            Student student = new Student();
            student.setName("Demo Student %02d".formatted(index));
            student.setEmail("student%02d@example.com".formatted(index));
            students.add(student);
        }
        studentRepository.saveAll(students);
    }

    private void seedCourses() {
        long count = courseRepository.count();
        if (count >= TARGET_RECORDS) {
            return;
        }

        List<Instructor> instructors = instructorRepository.findAll();
        List<Course> courses = new ArrayList<>();
        for (long index = count + 1; index <= TARGET_RECORDS; index++) {
            Course course = new Course();
            course.setTitle("Demo Course %02d".formatted(index));
            course.setStatus(index % 4 == 0 ? CourseStatus.INACTIVE : CourseStatus.ACTIVE);
            course.setInstructor(instructors.get((int) ((index - 1) % instructors.size())));
            courses.add(course);
        }
        courseRepository.saveAll(courses);
    }

    private void seedEnrollments() {
        long count = enrollmentRepository.count();
        if (count >= TARGET_RECORDS) {
            return;
        }

        List<Student> students = studentRepository.findAll();
        List<Course> courses = courseRepository.findAll().stream()
                .filter(course -> CourseStatus.ACTIVE == course.getStatus())
                .toList();
        if (courses.isEmpty()) {
            courses = courseRepository.findAll();
        }

        List<StudentEnrollment> enrollments = new ArrayList<>();
        for (long index = count + 1; index <= TARGET_RECORDS; index++) {
            Student student = students.get((int) ((index - 1) % students.size()));
            Course course = courses.get((int) ((index - 1) % courses.size()));
            if (enrollmentRepository.existsByCourseIdAndStudentId(course.getId(), student.getId())) {
                continue;
            }

            StudentEnrollment enrollment = new StudentEnrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setEnrolledAt(LocalDateTime.now().minusDays(TARGET_RECORDS - index));
            enrollments.add(enrollment);
        }
        enrollmentRepository.saveAll(enrollments);
    }
}
