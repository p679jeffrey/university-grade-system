package com.university.gradesystem.dto;

public class CourseRequest {
    private String course_name;
    private String teacher_id;

    public CourseRequest() {}

    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }
    
    public String getTeacher_id() { return teacher_id; }
    public void setTeacher_id(String teacher_id) { this.teacher_id = teacher_id; }
}