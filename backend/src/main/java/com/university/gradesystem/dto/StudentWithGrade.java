package com.university.gradesystem.dto;

public class StudentWithGrade {
    private String student_id;
    private String name;
    private Integer score;

    public StudentWithGrade() {}

    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}