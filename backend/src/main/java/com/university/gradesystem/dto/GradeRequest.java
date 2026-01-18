package com.university.gradesystem.dto;

public class GradeRequest {
    private String student_id;
    private Integer course_id;
    private Integer score;

    public GradeRequest() {}

    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    
    public Integer getCourse_id() { return course_id; }
    public void setCourse_id(Integer course_id) { this.course_id = course_id; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}