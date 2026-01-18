package com.university.gradesystem.dto;

public class StudentGradeResponse {
    private String course_name;
    private String teacher_name;
    private Integer score;

    public StudentGradeResponse() {}

    public String getCourse_name() { return course_name; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }
    
    public String getTeacher_name() { return teacher_name; }
    public void setTeacher_name(String teacher_name) { this.teacher_name = teacher_name; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}