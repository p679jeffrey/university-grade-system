package com.university.gradesystem.dto;

public class MessageResponse {
    private String message;
    private String error;
    private Object data;

    public MessageResponse() {}

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}