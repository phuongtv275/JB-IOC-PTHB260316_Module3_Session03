package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.Course;

public class EnrollmentDetail {
    private Long id;
    private String studentName;
    private Course course;

    public EnrollmentDetail(Long id, String studentName, Course course) {
        this.id = id;
        this.studentName = studentName;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public Course getCourse() {
        return course;
    }
}
