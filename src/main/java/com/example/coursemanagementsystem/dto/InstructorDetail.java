package com.example.coursemanagementsystem.dto;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.Instructor;
import java.util.List;

public class InstructorDetail {
    private Instructor instructor;
    private List<Course> courses;

    public InstructorDetail(Instructor instructor, List<Course> courses) {
        this.instructor = instructor;
        this.courses = courses;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public List<Course> getCourses() {
        return courses;
    }
}
