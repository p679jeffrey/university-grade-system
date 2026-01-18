package com.university.gradesystem.dto;

public class LoginResponse {
    private String teacher_id;
    private String student_id;
    private String name;
    private String error;

    public LoginResponse() {}

    public LoginResponse(String id, String name) {
        if (id != null && id.startsWith("T")) {
            this.teacher_id = id;
        } else if (id != null) {
            this.student_id = id;
        }
        this.name = name;
    }

    public String getTeacher_id() { return teacher_id; }
    public void setTeacher_id(String teacher_id) { this.teacher_id = teacher_id; }
    
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}