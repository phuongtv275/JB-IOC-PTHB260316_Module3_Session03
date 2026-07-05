package com.example.coursemanagementsystem.model;

public class Enrollment {
    private Long id;
    private String studentName;
    private Long courseId;

    public Enrollment() {
    }

    public Enrollment(Long id, String studentName, Long courseId) {
        this.id = id;
        this.studentName = studentName;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
