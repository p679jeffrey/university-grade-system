package com.university.gradesystem.dto;

public class LoginRequest {
    private String teacher_id;
    private String student_id;
    private String password;

    public LoginRequest() {}

    public String getTeacher_id() { return teacher_id; }
    public void setTeacher_id(String teacher_id) { this.teacher_id = teacher_id; }
    
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}